package com.cc.custom.seek;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cc.custom.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/27.
 */
public class TXFrameSeekBar extends FrameLayout implements TXRangeSeekBar.TXOnRangeChangeListener {

    private static final String TAG = "TXFrameSeekBar";

    private MediaMetadataRetriever mRetriever;
    private int mDuration;
    private int mWidth;
    private int mHeight;
    private MyAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                mAdapter.addData((TXVideoInfoModel) msg.getData().getSerializable("model"));
            }
        }
    };
    private TXRangeSeekBar mSeekBar;
    private int mCount;
    private int mOffset;
    private TXOnRangeChangedListener mListener;
    private boolean mIsRvMoved;
    private RecyclerView mRv;
    private int mStartTime;
    private int mEndTime;
    private int mRvOffset;
    private ImageView mIvPosition;
    private int mPadding;
    private ValueAnimator mAnimator;
    private int mDistance;
    private int mRangeStart;
    private int mRangeEnd;

    public TXFrameSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public TXFrameSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXFrameSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tx_layout_seek_bar, this);
        mSeekBar = (TXRangeSeekBar) view.findViewById(R.id.seekBar);
        mSeekBar.setOnRangeChangeListener(this);
        mIvPosition = (ImageView) view.findViewById(R.id.iv_position);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new MyAdapter();
        mRv.setAdapter(mAdapter);

        mHeight = getResources().getDimensionPixelOffset(R.dimen.tx_frame_height);
        mPadding = getResources().getDimensionPixelOffset(R.dimen.tx_range_seek_bar_padding);
        mWidth = (getResources().getDisplayMetrics().widthPixels - mPadding * 2) / 10;
    }

    public void setVideoPath(String path) {
        mRetriever = new MediaMetadataRetriever();
        mRetriever.setDataSource(path);
        String duration = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        mDuration = Integer.parseInt(duration);
        Log.d(TAG, "mDuration " + mDuration);

        mStartTime = 0;
        mRangeStart = 0;
        mCount = 10;
        mOffset = 6 * 1000;
        float minScale;
        if (mDuration > 60 * 1000) {
            mCount = mDuration / (6 * 1000);
            if (mDuration % (6 * 1000) > 0) {
                mCount++;
            }
            minScale = 3f / 60f;
            mEndTime = 60 * 1000;
            mRangeEnd = 60 * 1000;
            mRv.addOnScrollListener(mOnScrollListener);
        } else {
            mOffset = mDuration / 10;
            minScale = 3 * 1000f / mDuration;
            mEndTime = mDuration;
            mRangeStart = mDuration;
        }
        mSeekBar.setMinScale(minScale);

        getFrames();
    }

    public void anim() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }

        if (!mIvPosition.isShown()) {
            mIvPosition.setVisibility(View.VISIBLE);
        }

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvPosition.getLayoutParams();
        int start = mPadding - mDistance + mWidth * mStartTime / mOffset;
        int end = mPadding - mDistance + mWidth * mEndTime / mOffset;
        mAnimator = ValueAnimator.ofInt(start, end).setDuration(mEndTime - mStartTime);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                mIvPosition.setLayoutParams(params);
            }
        });
        mAnimator.start();
    }

    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dip * scale + 0.5F);
    }

    private void getFrames() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<TXVideoInfoModel> list = new ArrayList<>(mCount);

                for (int i = 0; i < mCount; i++) {
                    File file = new File("/mnt/sdcard/DCIM/CustomView/" + System.currentTimeMillis() + ".jpg");
                    try {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap thbBitmap = Bitmap.createScaledBitmap(mRetriever.getFrameAtTime(i * mOffset * 1000 + 1),
                        mWidth, mHeight, false);

                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(file);
                        thbBitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TXVideoInfoModel model = new TXVideoInfoModel();
                    model.path = file.getAbsolutePath();
                    model.startTime = i * mOffset;
                    model.endTime = (i + 1) * mOffset > mDuration ? mDuration : (i + 1) * mOffset;
                    long duration = model.endTime - model.startTime;
                    if (duration == mOffset) {
                        model.width = mWidth;
                    } else {
                        model.width = mWidth * duration / mOffset;
                    }

                    list.add(model);
                    Message message = mHandler.obtainMessage(100);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    public void onChange(float startPosition, float endPosition, int status) {
        Log.d(TAG, "onChange startPosition " + startPosition + ", endPosition " + endPosition + ", status " + status);
        if (mDuration <= 60 * 1000) {
            mRangeStart = (int) (mDuration * startPosition);
            mRangeEnd = (int) (mDuration * endPosition);
        } else {
            mRangeStart = (int) (60 * 1000 * startPosition);
            mRangeEnd = (int) (60 * 1000 * endPosition);
        }
        mStartTime = mRangeStart + mRvOffset;
        mEndTime = mRangeEnd + mRvOffset;

        if (status == TXRangeSeekBar.STATUS_DOWN) {
            mListener.onChanged(-1, Const.STATUS_PAUSE);
            if (mIvPosition.isShown()) {
                mIvPosition.setVisibility(View.GONE);
            }
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
            }
        } else if (status == TXRangeSeekBar.STATUS_MOVE_BOTH || status == TXRangeSeekBar.STATUS_MOVE_LEFT) {
            mListener.onChanged(mStartTime, Const.STATUS_MOVE);
        } else if (status == TXRangeSeekBar.STATUS_MOVE_RIGHT) {
            mListener.onChanged(mEndTime, Const.STATUS_MOVE);
        } else if (status == TXRangeSeekBar.STATUS_UP) {
            mListener.onChanged(mStartTime, Const.STATUS_PLAY);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<TXVideoInfoModel> mList = new ArrayList<>();

        public void setList(List<TXVideoInfoModel> list) {
            this.mList = list;
            this.notifyDataSetChanged();
        }

        public synchronized void addData(TXVideoInfoModel model) {
            mList.add(model);
            this.notifyItemInserted(mList.size());
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_edit, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            TXVideoInfoModel item = mList.get(position);

            ViewGroup.LayoutParams lp = holder.iv.getLayoutParams();
            lp.width = (int) item.width;
            lp.height = mHeight;
            holder.iv.setLayoutParams(lp);

            holder.iv.setImageBitmap(BitmapFactory.decodeFile(item.path));
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        private ImageView iv;

        public MyHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_thumb);
        }
    }

    public void setListener(TXOnRangeChangedListener listener) {
        this.mListener = listener;
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, "onScrollStateChanged " + newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mIsRvMoved = false;
                mStartTime = mRangeStart + mRvOffset;
                mEndTime = mRangeEnd + mRvOffset;
                mListener.onChanged(mStartTime, Const.STATUS_PLAY);
            } else {
                mIsRvMoved = true;
                mListener.onChanged(-1, Const.STATUS_PAUSE);
                if (mIvPosition.isShown()) {
                    mIvPosition.setVisibility(View.GONE);
                }
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d(TAG, "onScrolled dx" + dx);
            if (dx != 0) {
                mIsRvMoved = true;
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRv.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                View view = layoutManager.findViewByPosition(position);
                int width = view.getWidth();
                mDistance = width * position + mRv.getPaddingLeft() - view.getLeft();
                mRvOffset = mDistance * mOffset / width;
                Log.d(TAG, "distance " + mDistance + ", mRvOffset " + mRvOffset);
                mStartTime = mRvOffset;
                mEndTime = mRvOffset + mEndTime;
                mListener.onChanged(mStartTime, Const.STATUS_MOVE);
            }
        }
    };

    public int getStartTime() {
        return mStartTime;
    }

    public int getEndTime() {
        return mEndTime;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRv != null && mDuration > 60 * 1000) {
            mRv.removeOnScrollListener(mOnScrollListener);
        }
    }

    public interface TXOnRangeChangedListener {
        void onChanged(int playTime, int status);
    }
}
