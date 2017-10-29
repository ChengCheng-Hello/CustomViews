package com.cc.custom.seek;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cc.custom.R;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/27.
 */
public class TXRangeSeekBar extends View {

    private static final String TAG = "TXRangeSeekBar";

    private static final int TOUCH_OFFSET_W = 40;
    private static final int TOUCH_OFFSET_H = 20;

    private int mWidth;
    private int mHeight;
    private float mMinSpace = -1;
    private float mMinScale = -1;
    private int mLineWidth = 5;

    private Drawable mLeftSliderDrawable;
    private Drawable mRightSliderDrawable;
    private Rect mLeftSliderRect;
    private Rect mRightSliderRect;

    private Paint mSliderPaint;
    private Paint mMaxRangePaint;

    private int mSliderWidth;
    private int mSliderLeft;
    private int mSliderRight;
    private boolean mIsLeftDown;
    private boolean mIsRightDown;
    private int mLeftPointerId = -1;
    private int mRightPointerId = -1;
    private float mLeftPointLastX;
    private float mRightPointLastX;
    private float mLeftMoveX;
    private float mRightMoveX;

    private float mStartPosition = 0f;
    private float mEndPosition = 100f;
    private TXOnRangeChangeListener mListener;

    public TXRangeSeekBar(Context context) {
        this(context, null);
    }

    public TXRangeSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXRangeSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLeftSliderDrawable = getResources().getDrawable(R.drawable.handle_left);
        mRightSliderDrawable = getResources().getDrawable(R.drawable.handle_left);

        mLeftSliderRect = new Rect();
        mRightSliderRect = new Rect();

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

        mSliderWidth = 40;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getWidth();
        mSliderLeft = getPaddingLeft();
        mSliderRight = mWidth - getPaddingRight();
        mHeight = getHeight();

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMinSpace < 0 && mMinScale > 0) {
            mMinSpace = (mWidth - getPaddingLeft() - getPaddingRight()) * mMinScale;
        }

        // 最长范围线
        if (mIsLeftDown || mIsRightDown) {
            canvas.drawRect(getPaddingLeft() + mLineWidth / 2, mLineWidth / 2,
                    mWidth - getPaddingRight() - mLineWidth / 2, mHeight - mLineWidth / 2, mMaxRangePaint);
        }

        calcLeft();
        calcRight();
        // 左滑块
        mLeftSliderDrawable.setBounds(mSliderLeft, 0, mSliderLeft + mSliderWidth, mHeight);
        mLeftSliderDrawable.draw(canvas);
        // 右滑块
        mRightSliderDrawable.setBounds(mSliderRight - mSliderWidth, 0, mSliderRight, mHeight);
        mRightSliderDrawable.draw(canvas);
        // 上下滑线
        canvas.drawLine(mSliderLeft + mSliderWidth, mLineWidth / 2, mSliderRight - mSliderWidth, mLineWidth / 2,
                mSliderPaint);
        canvas.drawLine(mSliderLeft + mSliderWidth, mHeight - mLineWidth / 2, mSliderRight - mSliderWidth,
                mHeight - mLineWidth / 2, mSliderPaint);
    }

    private void calcLeft() {
        float dWidth = mWidth - getPaddingLeft() - getPaddingRight();
        mStartPosition = (mSliderLeft - getPaddingLeft()) / dWidth;
    }

    private void calcRight() {
        float dWidth = mWidth - getPaddingLeft() - getPaddingRight();
        mEndPosition = (mSliderRight - getPaddingLeft()) / dWidth;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                return handleDown(event);
            case MotionEvent.ACTION_MOVE:
                return handleMove(event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                return handleUp(event);
        }

        return super.onTouchEvent(event);
    }

    private boolean handleDown(MotionEvent event) {
        int actionIndex = MotionEventCompat.getActionIndex(event);
        if (actionIndex == -1) {
            return false;
        }
        float downX = event.getX(actionIndex);
        float downY = event.getY(actionIndex);

        if (downX >= mSliderLeft - TOUCH_OFFSET_W && downX <= mSliderLeft + mSliderWidth + TOUCH_OFFSET_W
                && downY >= 0 - TOUCH_OFFSET_H && downY <= mHeight + TOUCH_OFFSET_H) {
            mLeftPointerId = event.getPointerId(actionIndex);
            mLeftPointLastX = downX;
            mIsLeftDown = true;
            if (mListener != null) {
                mListener.onChange(mStartPosition, mEndPosition, STATUS_DOWN);
            }
            Log.d(TAG, "handleDown isLeftSlider " + mLeftPointerId + ", mLeftPointLastX " + downX);
            return true;
        } else if (downX >= mSliderRight - mSliderWidth - TOUCH_OFFSET_W && downX <= mSliderRight + TOUCH_OFFSET_W
                && downY >= 0 - TOUCH_OFFSET_H && downY <= mHeight + TOUCH_OFFSET_H) {
            mRightPointerId = event.getPointerId(actionIndex);
            mRightPointLastX = downX;
            mIsRightDown = true;
            if (mListener != null) {
                mListener.onChange(mStartPosition, mEndPosition, STATUS_DOWN);
            }
            Log.d(TAG, "handleDown isRightSlider " + mRightPointerId + ", mRightPointLastX " + downX);
            return true;
        } else {
            Log.d(TAG, "handlerDown other");
            return false;
        }
    }

    private boolean handleMove(MotionEvent event) {
        boolean move = false;
        if (mIsLeftDown && mLeftPointerId != -1) {
            int index = event.findPointerIndex(mLeftPointerId);
            if (index != -1) {
                float x = event.getX(index);
                mLeftMoveX = x - mLeftPointLastX;
                mLeftPointLastX = x;
                mSliderLeft += mLeftMoveX;
                if (mSliderLeft < getPaddingLeft()) {
                    mSliderLeft = getPaddingLeft();
                }
                // 不能超过右滑块，还有最小间距
                if (mSliderLeft >= mSliderRight - mSliderWidth * 2 - mMinSpace) {
                    mSliderLeft = (int) (mSliderRight - mSliderWidth * 2 - mMinSpace);
                }
                calcLeft();
                Log.d(TAG, "handleMoveLeft " + mLeftMoveX);
                move = true;
                invalidate();
            }
        }
        if (mIsRightDown && mRightPointerId != -1) {
            int index = event.findPointerIndex(mRightPointerId);
            if (index != -1) {
                float x = event.getX(index);
                mRightMoveX = x - mRightPointLastX;
                mRightPointLastX = x;
                mSliderRight += mRightMoveX;
                if (mSliderRight > mWidth - getPaddingRight()) {
                    mSliderRight = mWidth - getPaddingRight();
                }
                // 不能超过左滑块，还有最小间距
                if (mSliderRight <= mSliderLeft + mSliderWidth * 2 + mMinSpace) {
                    mSliderRight = (int) (mSliderLeft + mSliderWidth * 2 + mMinSpace);
                }
                calcRight();
                Log.d(TAG, "handleMoveRight " + mRightMoveX);
                move = true;
                invalidate();
            }
        }
        if (move) {
            if (mListener != null) {
                mListener.onChange(mStartPosition, mEndPosition, STATUS_MOVE);
            }
        }
        return move;
    }

    private boolean handleUp(MotionEvent event) {
        int actionIndex = MotionEventCompat.getActionIndex(event);
        if (actionIndex == -1) {
            return false;
        }
        int pointerId = event.getPointerId(actionIndex);
        if (pointerId == mLeftPointerId) {
            if (!mIsLeftDown) {
                return false;
            }
            calcLeft();
            if (mListener != null) {
                mListener.onChange(mStartPosition, mEndPosition, STATUS_UP);
            }
            mLeftPointLastX = 0;
            mLeftPointerId = -1;
            mIsLeftDown = false;
            Log.d(TAG, "handleUpLeft");
            invalidate();
            return true;
        } else if (pointerId == mRightPointerId) {
            if (!mIsRightDown) {
                return false;
            }
            calcRight();
            if (mListener != null) {
                mListener.onChange(mStartPosition, mEndPosition, STATUS_UP);
            }
            mRightPointLastX = 0;
            mRightPointerId = -1;
            mIsRightDown = false;
            Log.d(TAG, "handleUpRight");
            invalidate();
            return true;
        } else {
            Log.d(TAG, "handleUpOther");
            return false;
        }
    }

    public void setOnRangeChangeListener(TXOnRangeChangeListener listener) {
        this.mListener = listener;
    }

    public void setMinScale(float min) {
        this.mMinScale = min;
    }


    public interface TXOnRangeChangeListener {
        void onChange(float startPosition, float endPosition, int status);
    }

    public static final int STATUS_DOWN = 0;
    public static final int STATUS_MOVE = 1;
    public static final int STATUS_UP = 2;
}
