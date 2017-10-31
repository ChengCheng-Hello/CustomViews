package com.cc.custom.seek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.cc.custom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/31.
 */
public class TXFrameView extends View {

    private static final String TAG = "TXFrameView";

    private static final int TOUCH_OFFSET_W = 40;
    private static final int TOUCH_OFFSET_H = 20;

    private int mWidth;
    private int mHeight;
    private int mLineWidth = 5;

    private Paint mBitmapPaint;
    private Paint mSliderPaint;
    private Paint mMaxRangePaint;
    private Paint mCoverPaint;

    private int mSliderLeft;
    private int mSliderRight;

    private OverScroller mScroller;

    private TXRangeSeekBar3.TXOnRangeChangeListener mListener;

    private float mScrollDistanceX;
    private boolean mIsBitmapScrolled;
    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mGestureDetectorListener =
        new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                mScroller.forceFinished(true);
                mScroller.fling((int) mCurrentX, 0, (int) velocityX, 0, (int) -mBitmapWidth, (int) mBitmapWidth, 0, 0);
                ViewCompat.postInvalidateOnAnimation(TXFrameView.this);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "onSrcoll distanceX" + distanceX + " distanceY " + distanceY);

                mIsBitmapScrolled = true;
                mScrollDistanceX = distanceX;
                ViewCompat.postInvalidateOnAnimation(TXFrameView.this);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                mScroller.forceFinished(true);
                return true;
            }
        };
    private MediaMetadataRetriever mRetriever;
    private int mItemWidth;
    private int mItemDuration;
    private int mDuration;
    private int mCount;
    private float mCurrentX;

    public TXFrameView(Context context) {
        this(context, null);
    }

    public TXFrameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setVideoPath(String videoPath) {
        mRetriever = new MediaMetadataRetriever();
        mRetriever.setDataSource(videoPath);

        String durationStr = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mDuration = Integer.parseInt(durationStr);

        mCount = 10;
        mItemDuration = 6 * 1000;
        if (mDuration > 6 * 1000) {
            mCount = mDuration / (6 * 1000);
            if (mDuration % (6 * 1000) > 0) {
                mCount++;
            }
        } else {
            mItemDuration = mDuration / 10;
        }

        mScroller = new OverScroller(getContext());

        getBitmapWidth();
        getFrames();
    }

    private synchronized void getBitmapWidth() {
        if (mItemWidth > 0 && mBitmapWidth == 0) {
            for (int i = 0; i < mCount; i++) {
                if (i == mCount - 1) {
                    int startTime = i * mItemDuration;
                    int endTime = mDuration;
                    long duration = endTime - startTime;
                    mLastWidth = mItemWidth * duration / mItemDuration;
                    mBitmapWidth = (mCount - 1) * mItemWidth + mLastWidth;
                }
            }
        }
    }

    private List<Bitmap> mList = new ArrayList<>(mCount);
    private float mLastWidth;
    private float mBitmapWidth;

    private void getFrames() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = mCount;
                for (int i = 0; i < count; i++) {
                    Bitmap bitmap;
                    if (i == count - 1) {
                        bitmap = Bitmap.createScaledBitmap(mRetriever.getFrameAtTime(i * mItemDuration * 1000 + 1),
                            (int) mLastWidth, mHeight, false);
                    } else {
                        bitmap = Bitmap.createScaledBitmap(mRetriever.getFrameAtTime(i * mItemDuration * 1000 + 1),
                            mItemWidth, mHeight, false);
                    }

                    mList.add(bitmap);
                    postInvalidate();
                }
            }
        }).start();
    }

    private void init() {

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mSliderPaint = new Paint();
        mSliderPaint.setAntiAlias(true);
        mSliderPaint.setColor(Color.WHITE);
        mSliderPaint.setStyle(Paint.Style.STROKE);
        mSliderPaint.setStrokeWidth(mLineWidth);

        mMaxRangePaint = new Paint();
        mMaxRangePaint.setAntiAlias(true);
        mMaxRangePaint.setColor(Color.WHITE);
        mMaxRangePaint.setStyle(Paint.Style.STROKE);
        mMaxRangePaint.setStrokeWidth(mLineWidth);

        mCoverPaint = new Paint();
        mCoverPaint.setAntiAlias(true);
        mCoverPaint.setColor(ContextCompat.getColor(getContext(), R.color.TX_CO_BLACK_30));
        mCoverPaint.setStyle(Paint.Style.FILL);

        mGestureDetector = new GestureDetector(getContext(), mGestureDetectorListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getWidth();
        mSliderLeft = getPaddingLeft();
        mSliderRight = mWidth - getPaddingRight();
        mHeight = getHeight();
        mItemWidth = (mWidth - getPaddingLeft() - getPaddingRight()) / 10;
        getBitmapWidth();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = mWidth;
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    public void setSliderPosition(int left, int right) {
        this.mSliderLeft = left;
        this.mSliderRight = right;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIsBitmapScrolled) {
            mCurrentX -= mScrollDistanceX;
            if (mCurrentX > 0) {
                Log.d(TAG, "mCurrentX " + mCurrentX + ", mSliderLeft " + mSliderLeft);
                if (mCurrentX > mSliderLeft - getPaddingLeft()) {
                    mCurrentX = mSliderLeft - getPaddingLeft();
                }
            } else if (mCurrentX < 0) {
                float mBitmapScrollMaxWidth = mBitmapWidth - mSliderRight + getPaddingLeft();
                Log.d(TAG, "mCurrentX " + mCurrentX + ", mBitmapScrollMaxWidth " + mBitmapScrollMaxWidth);
                if (mBitmapScrollMaxWidth > 0) {
                    if (Math.abs(mCurrentX) > mBitmapScrollMaxWidth) {
                        mCurrentX = -mBitmapScrollMaxWidth;
                    }
                }
            }
        } else {
            if (mCurrentX != 0 && mSliderLeft <= mCurrentX + getPaddingLeft()) {
                // 左滑块往左滑，bitmap不在起点，在左滑块的左边，将跟随左滑块往左滑动
                mCurrentX = mSliderLeft - getPaddingLeft();
            } else if (mCurrentX != 0 && mSliderRight >= mCurrentX + getPaddingLeft() + mBitmapWidth) {
                // 右滑块往右滑，bitmap不在终点，在右滑块的右边，将跟随右滑块往右滑动
                mCurrentX = mSliderRight - getPaddingLeft() - mBitmapWidth;
            }
        }

        // bitmap
        for (int i = 0, len = mList.size(); i < len; i++) {
            float left = getPaddingLeft() + i * mItemWidth + mCurrentX;
            int top = 0;
            canvas.drawBitmap(mList.get(i), left, top, mBitmapPaint);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mCurrentX = mScroller.getCurrX();
            if (mCurrentX > 0) {
                if (mCurrentX > mSliderLeft - getPaddingLeft()) {
                    mCurrentX = mSliderLeft - getPaddingLeft();
                }
            } else if (mCurrentX < 0) {
                float mBitmapScrollMaxWidth = mBitmapWidth - mSliderRight + getPaddingLeft();
                if (mBitmapScrollMaxWidth > 0) {
                    if (Math.abs(mCurrentX) > mBitmapScrollMaxWidth) {
                        mCurrentX = -mBitmapScrollMaxWidth;
                    }
                }
            }
            Log.d(TAG, "computeScroll mCurrentX " + mCurrentX);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBitmapScrolled = false;
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    public static final int STATUS_DOWN = 0;
    public static final int STATUS_MOVE = 1;
    public static final int STATUS_UP = 2;
}
