package com.cc.stepview.stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.cc.stepview.R;

import java.util.List;

/**
 * Created by Cheng on 16/7/4.
 */
public class StepView extends View {

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

    private List<StepItem> mStepData;
    private int mMaxTextWidth;

    private int mWidth;

    private int mTouchSlop;
    private boolean mIsBeingDragged;
    private int mLastMotionX;

    public StepView(Context context) {
        this(context, null, 0);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
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

        if (mStepData != null) {
            for (StepItem item : mStepData) {
                float tipWidth = mPaintTextDone.measureText(item.tip);
                maxWidth = Math.max(maxWidth, tipWidth);
                float dateWidth = mPaintTextDate.measureText(item.date);
                maxWidth = Math.max(maxWidth, dateWidth);

            }
        }

        return Math.round(maxWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d(TAG, "onDraw");
        super.onDraw(canvas);

        if (mStepData == null) {
            String loading = "Loading...";
            float loadingWidth = mPaintTextDate.measureText(loading);
            canvas.drawText(loading, (getWidth() - loadingWidth) / 2, getHeight() / 2, mPaintTextDate);
            return;
        }

        int startCircleX;
        if (mMaxTextWidth > mCircleRadius) {
            startCircleX = getPaddingLeft() + mMaxTextWidth / 2;
        } else {
            startCircleX = mCircleRadius + getPaddingLeft();
        }

        int startLineX = startCircleX + mCircleRadius;
        int startY = mCircleRadius + getPaddingTop();

        for (int i = 0; i < mStepCount; i++) {
            StepItem stepItem = mStepData.get(i);

            float statusTextX = startCircleX - mPaintTextDone.measureText(stepItem.tip) / 2;
            float dateTextX = startCircleX - mPaintTextDate.measureText(stepItem.date) / 2;

            float statusTextY = startY + mCircleRadius + mTextSpace + mStatusTextSize;
            float dateTextY = startY + mCircleRadius + mTextSpace * 2 + mStatusTextSize + mDateTextSize;

            if (i < mDoneStep) {
                // 完成
                canvas.drawCircle(startCircleX, startY, mCircleRadius, mPaintCircleDone);
                if (i < mStepCount - 1) {
                    canvas.drawLine(startLineX, startY, startLineX + mLineWidth, startY, mPaintLineDone);
                }
                canvas.drawText(stepItem.tip, statusTextX, statusTextY, mPaintTextDone);
                canvas.drawText(stepItem.date, dateTextX, dateTextY, mPaintTextDate);
            } else {
                // 没完成
                canvas.drawCircle(startCircleX, startY, mCircleRadius, mPaintCircleDefault);
                if (i < mStepCount - 1) {
                    canvas.drawLine(startLineX, startY, startLineX + mLineWidth, startY, mPaintLineDefault);
                }
                canvas.drawText(stepItem.tip, statusTextX, statusTextY, mPaintTextDefault);
                canvas.drawText(stepItem.date, dateTextX, dateTextY, mPaintTextDate);
            }

            startCircleX += mLineWidth + mCircleRadius * 2;
            startLineX = startCircleX + mCircleRadius;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mLastMotionX = (int) ev.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int x = (int) ev.getX();
                int deltaX = mLastMotionX - x;
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionX = x;

                    final int range = getScrollRange();

                    // Calling overScrollBy will call onOverScrolled, which
                    // calls onScrollChanged if applicable.
                    overScrollBy(deltaX, 0, getScrollX(), 0, range, 0, 0, 0, true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                break;
        }
        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY,
                                  boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
    }


    private int getScrollRange() {
        return mWidth - getWidth();
    }

    public void initData(int stepCount, int doneCount, List<StepItem> stepData) {
        mStepCount = stepCount;
        mDoneStep = doneCount;
        mStepData = stepData;
        requestLayout();
    }
}
