package com.cc.custom.stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.cc.custom.R;

import java.util.List;

/**
 * Created by Cheng on 16/7/4.
 */
public class StepView2 extends View {

    private static final String TAG = "StepView";

    private int mColorDefault = ContextCompat.getColor(getContext(), R.color.colorPrimary);
    private int mColorDone = ContextCompat.getColor(getContext(), R.color.colorAccent);

    private int mCircleRadius;
    private int mLineHeight;

    private int mCircleColorDefault;
    private int mCircleColorDone;
    private int mLineColorDefault;
    private int mLineColorDone;
    private int mTextColorDefault;
    private int mTextColorDone;
    private int mTextDateColor;

    private int mStatusTextSize;
    private int mDateTextSize;

    private Paint mPaintCircleDefault;
    private Paint mPaintLineDefault;
    private Paint mPaintCircleDone;
    private Paint mPaintLineDone;
    private Paint mPaintTextDefault;
    private Paint mPaintTextDone;
    private Paint mPaintTextDate;

    private int mLineWidth;
    private int mTextSpace;

    private int mStepCount;
    private int mDoneStep;

    private List<String> mStatusData;
    private List<String> mDateData;
    private int mMaxTextWidth;

    private int mWidth;

    private float mDistanceX;
    private GestureDetector mGestureDestector;
    private GestureDetector.SimpleOnGestureListener mGestureDetectorListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            System.out.println("@@@ onSrcoll  distanceX " + distanceX + " distanceY " + distanceY);

            mDistanceX = distanceX;
            ViewCompat.postInvalidateOnAnimation(StepView2.this);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    };
    private float mCurrentX;

    public StepView2(Context context) {
        this(context, null, 0);
    }

    public StepView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        if (typedArray != null) {
            mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.StepView_circleRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getContext().getResources().getDisplayMetrics()));
            mLineHeight = typedArray.getDimensionPixelSize(R.styleable.StepView_lineHeight, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics()));
            mCircleColorDefault = typedArray.getColor(R.styleable.StepView_circleColorDefault, mColorDefault);
            mCircleColorDone = typedArray.getColor(R.styleable.StepView_circleColorDone, mColorDone);
            mLineColorDefault = typedArray.getColor(R.styleable.StepView_lineColorDefault, mColorDefault);
            mLineColorDone = typedArray.getColor(R.styleable.StepView_lineColorDone, mColorDone);
            mTextColorDefault = typedArray.getColor(R.styleable.StepView_statusTextColorDone, mColorDefault);
            mTextColorDone = typedArray.getColor(R.styleable.StepView_statusTextColorDefault, mColorDone);
            mTextDateColor = typedArray.getColor(R.styleable.StepView_textDateColor, mColorDefault);
            mStatusTextSize = typedArray.getDimensionPixelSize(R.styleable.StepView_statusTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getContext().getResources().getDisplayMetrics()));
            mDateTextSize = typedArray.getDimensionPixelSize(R.styleable.StepView_dateTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getContext().getResources().getDisplayMetrics()));
            mTextSpace = typedArray.getDimensionPixelSize(R.styleable.StepView_textSpace, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics()));
            mStepCount = typedArray.getInt(R.styleable.StepView_stepCount, 3);
            typedArray.recycle();
        }

        initPaints();
        mGestureDestector = new GestureDetector(context, mGestureDetectorListener);
    }

    private void initPaints() {
        mPaintCircleDefault = new Paint();
        mPaintCircleDefault.setColor(mCircleColorDefault);
        mPaintCircleDefault.setStyle(Paint.Style.FILL);
        mPaintCircleDefault.setAntiAlias(true);

        mPaintCircleDone = new Paint();
        mPaintCircleDone.setColor(mCircleColorDone);
        mPaintCircleDone.setStyle(Paint.Style.FILL);
        mPaintCircleDone.setAntiAlias(true);

        mPaintLineDefault = new Paint();
        mPaintLineDefault.setColor(mLineColorDefault);
        mPaintLineDefault.setStyle(Paint.Style.FILL);
        mPaintLineDefault.setStrokeWidth(mLineHeight);
        mPaintLineDefault.setAntiAlias(true);

        mPaintLineDone = new Paint();
        mPaintLineDone.setColor(mLineColorDone);
        mPaintLineDone.setStyle(Paint.Style.FILL);
        mPaintLineDone.setStrokeWidth(mLineHeight);
        mPaintLineDone.setAntiAlias(true);

        mPaintTextDefault = new Paint();
        mPaintTextDefault.setColor(mTextColorDefault);
        mPaintTextDefault.setAntiAlias(true);
        mPaintTextDefault.setTextSize(mStatusTextSize);

        mPaintTextDone = new Paint();
        mPaintTextDone.setColor(mTextColorDone);
        mPaintTextDone.setAntiAlias(true);
        mPaintTextDone.setTextSize(mStatusTextSize);

        mPaintTextDate = new Paint();
        mPaintTextDate.setColor(mTextDateColor);
        mPaintTextDate.setAntiAlias(true);
        mPaintTextDate.setTextSize(mDateTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMaxTextWidth = getMaxTextWidth();
        mLineWidth = mMaxTextWidth;

        mWidth = (mStepCount - 1) * mLineWidth + mStepCount * mCircleRadius * 2;
        if (mMaxTextWidth > mCircleRadius * 2) {
            mWidth += mMaxTextWidth - mCircleRadius * 2;
        }
        int mHeight = mCircleRadius * 2 + mTextSpace + mStatusTextSize + mTextSpace + mDateTextSize;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = mWidth + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mHeight + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    private int getMaxTextWidth() {
        float maxWidth = 100;

        if (mStatusData != null) {
            for (String text : mStatusData) {
                float width = mPaintTextDone.measureText(text);
                maxWidth = Math.max(maxWidth, width);
            }
        }

        if (mDateData != null) {
            for (String text : mDateData) {
                float width = mPaintTextDate.measureText(text);
                maxWidth = Math.max(maxWidth, width);
            }
        }

        return Math.round(maxWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d(TAG, "onDraw");
        super.onDraw(canvas);

        mCurrentX -= mDistanceX;

        if (mStatusData == null || mDateData == null) {
            return;
        }

        int startCircleX;
        if (mMaxTextWidth > mCircleRadius) {
            startCircleX = (int) (getPaddingLeft() + mMaxTextWidth / 2 + mCurrentX);
        } else {
            startCircleX = (int) (mCircleRadius + getPaddingLeft() + mCurrentX);
        }

        int startLineX = startCircleX + mCircleRadius;
        int startY = mCircleRadius + getPaddingTop();

        for (int i = 0; i < mStepCount; i++) {
            float statusTextX = startCircleX - mPaintTextDone.measureText(mStatusData.get(i)) / 2;
            float dateTextX = startCircleX - mPaintTextDate.measureText(mDateData.get(i)) / 2;

            float statusTextY = startY + mCircleRadius + mTextSpace + mStatusTextSize;
            float dateTextY = startY + mCircleRadius + mTextSpace * 2 + mStatusTextSize + mDateTextSize;

            if (i < mDoneStep) {
                // 完成
                canvas.drawCircle(startCircleX, startY, mCircleRadius, mPaintCircleDone);
                if (i < mStepCount - 1) {
                    canvas.drawLine(startLineX, startY, startLineX + mLineWidth, startY, mPaintLineDone);
                }
                canvas.drawText(mStatusData.get(i), statusTextX, statusTextY, mPaintTextDone);
                canvas.drawText(mDateData.get(i), dateTextX, dateTextY, mPaintTextDate);
            } else {
                // 没完成
                canvas.drawCircle(startCircleX, startY, mCircleRadius, mPaintCircleDefault);
                if (i < mStepCount - 1) {
                    canvas.drawLine(startLineX, startY, startLineX + mLineWidth, startY, mPaintLineDefault);
                }
                canvas.drawText(mStatusData.get(i), statusTextX, statusTextY, mPaintTextDefault);
                canvas.drawText(mDateData.get(i), dateTextX, dateTextY, mPaintTextDate);
            }

            startCircleX += mLineWidth + mCircleRadius * 2;
            startLineX = startCircleX + mCircleRadius;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDestector.onTouchEvent(ev);
    }

    private int getScrollRange() {
        return mWidth - getWidth();
    }

    public void initData(int stepCount, int doneCount, List<String> statusData, List<String> dateData) {
        mStepCount = stepCount;
        mDoneStep = doneCount;
        mStatusData = statusData;
        mDateData = dateData;
        requestLayout();
    }
}
