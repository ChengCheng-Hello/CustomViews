package com.cc.custom.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/7/11.
 */
public class LineChartView2 extends View {

    private int mDoneColor;
    private int mDefaultColor;
    private int mTextDateColor;
    private int mTextDateSize;
    private int mTextValueColor;
    private int mTextValueSize;
    private int mDoneLineWidth;
    private int mDefaultLineWidth;
    private int mCircleRadius;
    private int mSpace;
    private int mTextSpace;

    private int mTextDateHeight;
    private int mTextValueHeight;
    private int mTextStartWidth;

    private Paint mDoneLinePaint;
    private Paint mDefaultLinePaint;
    private Paint mDoneCirclePaint;
    private Paint mDefaultCirclePaint;
    private Paint mDefaultCirclePaint2;
    private Paint mTextValuePaint;
    private Paint mTextDatePaint;

    private Path mDoneLinePath;
    private Path mDefaultLinePath;

    private DashPathEffect mDashPathEffect;

    private float mYMax = -Float.MAX_VALUE;
    private float mYMin = Float.MAX_VALUE;
    private float mYRange;

    private int mCurrentX;
    private float mDistanceX;

    private List<ChartItem> mDataList;
    private int mWidth;

    private OverScroller mScroller;
    private GestureDetectorCompat mGestureDetector;
    private final GestureDetector.SimpleOnGestureListener mGestureListener =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    mScroller.forceFinished(true);
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    mDistanceX = distanceX;
                    invalidate();
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    mScroller.forceFinished(true);
                    mScroller.fling(mCurrentX, 0, (int) velocityX, 0, -getScrollRange(), 0, 0, 0, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(LineChartView2.this);
                    return true;
                }

            };

    public LineChartView2(Context context) {
        this(context, null);
    }

    public LineChartView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // TODO
        mDoneColor = Color.parseColor("#41c5ee");
        mDefaultColor = Color.parseColor("#c4eefa");
        mTextDateColor = Color.parseColor("#666666");
        mTextValueColor = Color.parseColor("#8b8b8b");
        mDoneLineWidth = 8;
        mDefaultLineWidth = 6;
        mCircleRadius = 14;
        mTextDateSize = 40;
        mTextValueSize = 34;
        mSpace = 200;
        mTextSpace = 30;

        initPaints();

        mScroller = new OverScroller(context);
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
    }

    private void initPaints() {
        mDoneLinePaint = new Paint();
        mDoneLinePaint.setColor(mDoneColor);
        mDoneLinePaint.setStrokeWidth(mDoneLineWidth);
        mDoneLinePaint.setAntiAlias(true);
        mDoneLinePaint.setStyle(Paint.Style.STROKE);

        mDoneCirclePaint = new Paint();
        mDoneCirclePaint.setColor(mDoneColor);
        mDoneCirclePaint.setAntiAlias(true);
        mDoneCirclePaint.setStyle(Paint.Style.FILL);

        mTextDatePaint = new Paint();
        mTextDatePaint.setColor(mTextDateColor);
        mTextDatePaint.setTextSize(mTextDateSize);
        mTextDatePaint.setAntiAlias(true);
        mTextDatePaint.setStyle(Paint.Style.FILL);

        mTextValuePaint = new Paint();
        mTextValuePaint.setColor(mTextValueColor);
        mTextValuePaint.setTextSize(mTextValueSize);
        mTextValuePaint.setAntiAlias(true);
        mTextValuePaint.setStyle(Paint.Style.FILL);

        mDefaultLinePaint = new Paint();
        mDefaultLinePaint.setColor(mDefaultColor);
        mDefaultLinePaint.setStrokeWidth(mDefaultLineWidth);
        mDefaultLinePaint.setAntiAlias(true);
        mDefaultLinePaint.setStyle(Paint.Style.STROKE);

        mDefaultCirclePaint = new Paint();
        mDefaultCirclePaint.setColor(mDefaultColor);
        mDefaultCirclePaint.setAntiAlias(true);
        mDefaultCirclePaint.setStyle(Paint.Style.STROKE);
        mDefaultCirclePaint.setStrokeWidth(8);

        mDefaultCirclePaint2 = new Paint();
        mDefaultCirclePaint2.setColor(Color.WHITE);
        mDefaultCirclePaint2.setAntiAlias(true);
        mDefaultCirclePaint2.setStyle(Paint.Style.FILL);

        mDashPathEffect = new DashPathEffect(new float[]{20, 10}, 0);
        mDoneLinePath = new Path();
        mDefaultLinePath = new Path();

        Rect textDateRect = new Rect();
        mTextDatePaint.getTextBounds("1日", 0, 2, textDateRect);
        mTextDateHeight = textDateRect.height();
        mTextStartWidth = textDateRect.width();

        Rect textValueRect = new Rect();
        mTextValuePaint.getTextBounds("120", 0, 3, textValueRect);
        mTextValueHeight = textValueRect.height();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getWidth();
        int mHeight = getHeight() + getPaddingTop() + getPaddingBottom();
        if (mDataList != null) {
            mWidth = (mDataList.size() - 1) * mSpace + mTextStartWidth * 2 + getPaddingLeft() + getPaddingRight();
        }

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

        // 显示Loading...
        if (mDataList == null) {
            String loading = "Loading...";
            float loadingWidth = mTextDatePaint.measureText(loading);
            canvas.drawText(loading, (getWidth() - loadingWidth) / 2, getHeight() / 2, mTextDatePaint);
            return;
        }

        // No Data...
        if (mDataList.size() == 0) {
            String loading = "NO DATA";
            float loadingWidth = mTextDatePaint.measureText(loading);
            canvas.drawText(loading, (getWidth() - loadingWidth) / 2, getHeight() / 2, mTextDatePaint);
            return;
        }

        // 滚动范围限制
        if (mCurrentX - mDistanceX > 0) {
            mCurrentX = 0;
        } else if (mCurrentX - mDistanceX < -getScrollRange()) {
            mCurrentX = -getScrollRange();
        } else {
            mCurrentX -= mDistanceX;
        }

        mDoneLinePath.reset();
        mDefaultLinePath.reset();

        int height = getHeight();
        // 实际图表高度
        float chartHeight = height - getPaddingBottom() - getPaddingTop() - mTextDateHeight - mTextValueHeight - mCircleRadius * 2 - mTextSpace * 4;
        float rate = chartHeight / mYRange;

        float startX = getPaddingLeft() + mTextStartWidth + mCurrentX;
        float startY = height - getPaddingBottom();

        Log.d("hh", "height " + height + ", chartH " + chartHeight + ", mrange " + mYRange + ", startY " + startY + ", textstartwidth " + mTextStartWidth);

        for (int i = 0; i < mDataList.size(); i++) {
            ChartItem chartItem = mDataList.get(i);
            float x = startX + mSpace * i;
            float valueY = chartItem.value - mYMin;
            float y = startY - mTextDateHeight - mTextSpace * 2 - (valueY == 0 ? 1 : valueY) * rate;

//            Log.d("hh", "x " + x + ", y " + y + ", valueY " + valueY);

            if (i == 0) {
                mDoneLinePath.moveTo(x, y);
                mDefaultLinePath.moveTo(x, y);
            } else {
                if (chartItem.isDone) {
                    mDoneLinePath.lineTo(x, y);
                    mDefaultLinePath.reset();
                    mDefaultLinePath.moveTo(x, y);
                } else {
                    mDefaultLinePath.lineTo(x, y);
                }
            }

            // 计算点
            chartItem.x = x;
            chartItem.y = y;

            // 计算顶部文字
            chartItem.valueX = x - (mTextValuePaint.measureText(String.valueOf(chartItem.value)) / 2);
            chartItem.valueY = y - mCircleRadius - mTextSpace;

            // 计算底部文字
            chartItem.dateX = x - (mTextDatePaint.measureText(chartItem.date) / 2);
            chartItem.dateY = startY - mTextSpace;
        }

        // 划线
        canvas.drawPath(mDoneLinePath, mDoneLinePaint);
        mDefaultLinePaint.setPathEffect(mDashPathEffect);
        canvas.drawPath(mDefaultLinePath, mDefaultLinePaint);

        for (ChartItem item : mDataList) {
            // 画圆点
            if (item.isDone) {
                canvas.drawCircle(item.x, item.y, mCircleRadius, mDoneCirclePaint);
            } else {
                canvas.drawCircle(item.x, item.y, mCircleRadius, mDefaultCirclePaint);
                canvas.drawCircle(item.x, item.y, mCircleRadius - 4, mDefaultCirclePaint2);
            }

            // 画顶部文字
            canvas.drawText(String.valueOf(item.value), item.valueX, item.valueY, mTextValuePaint);

            // 画底部日期文字
            canvas.drawText(item.date, item.dateX, item.dateY, mTextDatePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mCurrentX = mScroller.getCurrX();
            invalidate();
        }
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<ChartItem> data) {
        this.mDataList = data;
        calcMinMax();
        requestLayout();
    }

    /**
     * 滚动到指定位置
     *
     * @param position
     */
    public void smoothScrollTo(int position) {
        int dx = -position * mSpace - mCurrentX;
        mScroller.startScroll(mCurrentX, 0, dx, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 计算Y轴大小
     */
    private void calcMinMax() {
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }

        for (int i = 0; i < mDataList.size(); i++) {
            ChartItem item = mDataList.get(i);
            if (item.value > mYMax) {
                mYMax = item.value;
            }

            if (item.value < mYMin) {
                mYMin = item.value;
            }
        }

        mYRange = mYMax - mYMin;

        if (mYRange == 0) {
            mYRange = 2;
        }
    }

    /**
     * 计算滚动范围
     *
     * @return
     */
    private int getScrollRange() {
        int scrollRange = mWidth - getWidth();
        return scrollRange > 0 ? scrollRange : 0;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.dataList = mDataList;
        ss.yRange = mYRange;
        ss.yMin = mYMin;
        ss.currentX = mCurrentX;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(state);

        mDataList = ss.dataList;
        mYRange = ss.yRange;
        mYMin = ss.yMin;
        mCurrentX = ss.currentX;

        invalidate();
    }

    static class SavedState extends BaseSavedState {
        List<ChartItem> dataList;
        float yRange;
        float yMin;
        int currentX;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            dataList = new ArrayList<>();
            in.readList(dataList, ChartItem.class.getClassLoader());
            yRange = in.readFloat();
            yMin = in.readFloat();
            currentX = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeList(dataList);
            out.writeFloat(yRange);
            out.writeFloat(yMin);
            out.writeInt(currentX);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
