package com.cc.custom.seek;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
public class TXFrameSeekBar extends FrameLayout {

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
        RecyclerView mRv = (RecyclerView) view.findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new MyAdapter();
        mRv.setAdapter(mAdapter);

        mHeight = getResources().getDimensionPixelOffset(R.dimen.tx_frame_height);
        int padding = getResources().getDimensionPixelOffset(R.dimen.tx_range_seek_bar_padding);
        mWidth = (getResources().getDisplayMetrics().widthPixels - padding * 2) / 10;
    }

    public void setVideoPath(String path) {
        mRetriever = new MediaMetadataRetriever();
        mRetriever.setDataSource(path);
        String duration = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        mDuration = Integer.parseInt(duration);

        getFrames();
    }

    private void getFrames() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 10;
                int offset = 6 * 1000;
                if (mDuration > 60 * 1000) {
                    count = mDuration / 60 * 1000;
                    if (mDuration % 60 * 1000 > 0) {
                        count++;
                    }
                } else {
                    offset = mDuration / 10;
                }

                for (int i = 0; i < count; i++) {
                    File file = new File("/mnt/sdcard/Pictures/test_" + i + ".jpg");
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap thbBitmap = Bitmap.createScaledBitmap(mRetriever.getFrameAtTime(i * offset * 1000 + 1),
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
                    model.startTime = i * offset;
                    model.endTime = (i + 1) * offset > mDuration ? mDuration : (i + 1) * offset;
                    long duration = model.endTime - model.startTime;
                    if (duration == offset) {
                        model.width = mWidth;
                    } else {
                        model.width = mWidth * duration / offset;
                    }
                    Message message = mHandler.obtainMessage(100);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<TXVideoInfoModel> mList = new ArrayList<>();

        public void setList(List<TXVideoInfoModel> list) {
            this.mList = list;
            this.notifyDataSetChanged();
        }

        public void addData(TXVideoInfoModel model) {
            this.mList.add(model);
            this.notifyDataSetChanged();
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
}
