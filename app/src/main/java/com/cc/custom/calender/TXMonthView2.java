package com.cc.custom.calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXDateModel;
import com.cc.custom.calender.demo.model.TXMonthModel;

import java.util.List;

/**
 * 月日视图（单日／周模式）
 * <p>
 * Created by Cheng on 2017/9/25.
 */
public class TXMonthView2 extends View {

    private List<TXDateModel> mDayList;
    private int mWeekCount;
    private int mLastDayOfMonth;

    private int mRowHeight = 160;
    private int mColumnCount = 7;
    private int mColumnWidth;

    private Paint mDayTextPaint;
    private Paint mDayTextSelectPaint;
    private Paint mDaySelectBgPaint;
    private Paint mCurrentDayPaint;
    private Paint mLinePaint;

    @ColorInt
    private int mDayTextColor;
    @ColorInt
    private int mDayTextSelectColor;
    @ColorInt
    private int mDaySelectBgColor;

    private int mDayTextSize;
    private int mWidth;
    private float mTextHeight;
    private float mSelectBgRadius;
    private int mLineWidth = 1;

    private GestureDetectorCompat mGestureDetector;
    private TXOnSelectDateListener mSelectDateListener;

    public TXMonthView2(Context context) {
        this(context, null);
    }

    public TXMonthView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXMonthView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDayTextColor = Color.BLACK;
        mDayTextSize = 40;

        mDayTextSelectColor = Color.WHITE;
        mDaySelectBgColor = Color.BLUE;

        init();
    }

    public void setData(@NonNull TXMonthModel data, TXOnSelectDateListener listener) {
        this.mDayList = data.dayList;
        this.mWeekCount = data.weekCount;
        this.mLastDayOfMonth = data.lastDayOfMonth;
        this.mSelectDateListener = listener;

        requestLayout();
    }

    private void init() {
        mDayTextPaint = new Paint();
        mDayTextPaint.setAntiAlias(true);
        mDayTextPaint.setColor(mDayTextColor);
        mDayTextPaint.setTextSize(mDayTextSize);

        mDayTextSelectPaint = new Paint();
        mDayTextSelectPaint.setAntiAlias(true);
        mDayTextSelectPaint.setColor(mDayTextSelectColor);
        mDayTextSelectPaint.setTextSize(mDayTextSize);

        mDaySelectBgPaint = new Paint();
        mDaySelectBgPaint.setAntiAlias(true);
        mDaySelectBgPaint.setColor(mDaySelectBgColor);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setStyle(Paint.Style.FILL);

        mCurrentDayPaint = new Paint();
        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setColor(Color.GREEN);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setStrokeWidth(10);
        mCurrentDayPaint.setStrokeCap(Paint.Cap.ROUND);

        Rect textRect = new Rect();
        mDayTextPaint.getTextBounds("1", 0, 1, textRect);
        mTextHeight = textRect.height();

        mSelectBgRadius = mTextHeight * 1.8f;

        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                handleClickEvent(e);
                return super.onSingleTapUp(e);
            }
        });
        mGestureDetector.setIsLongpressEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        mWidth = getWidth();
        int height = mRowHeight * mWeekCount;

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = mWidth + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }
        }

        mColumnWidth = mWidth / mColumnCount;

        setMeasuredDimension(mWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLastDayOfMonth == 0) {
            return;
        }

        int count = mDayList.size();
        int index = 0;

        for (int i = 0; i < mWeekCount; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                if (index > count) {
                    break;
                }

                int centerX = mColumnWidth * j + mColumnWidth / 2;
                int centerY = mRowHeight * i + mRowHeight / 2 + mLineWidth * i;

                TXDateModel dataModel = mDayList.get(index);
                if (dataModel.date != null) {
                    // text
                    String content = String.valueOf(dataModel.date.getDay());
                    float textWidth = mDayTextPaint.measureText(content);

                    // bg
                    if (dataModel.isSelected) {
                        canvas.drawCircle(centerX, centerY, mSelectBgRadius, mDaySelectBgPaint);
                        canvas.drawText(content, centerX - textWidth / 2, centerY + mTextHeight / 2,
                            mDayTextSelectPaint);
                    } else {
                        canvas.drawText(content, centerX - textWidth / 2, centerY + mTextHeight / 2, mDayTextPaint);
                    }

                    // todayMark
                    if (dataModel.isShowTodayMark) {
                        canvas.drawPoint(centerX, centerY + mTextHeight + 6, mCurrentDayPaint);
                    }
                }

                index++;
            }

            if (i < mWeekCount - 1) {
                int lineY = mRowHeight * (i + 1);
                canvas.drawLine(0, lineY, mWidth, lineY, mLinePaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private void handleClickEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int colIndex = mColumnCount;
        for (int i = 0; i < mColumnCount; i++) {
            if (x > (colIndex - 1) * mColumnWidth) {
                break;
            }
            colIndex--;
        }
        int rowIndex = mWeekCount;
        for (int i = 0; i < mWeekCount; i++) {
            if (y > (rowIndex - 1) * mRowHeight) {
                break;
            }
            rowIndex--;
        }
        int index = colIndex + (rowIndex - 1) * mColumnCount - 1;
        if (index < 0 || index >= mDayList.size()) {
            return;
        }
        TXDateModel dataModel = mDayList.get(index);
        if (dataModel == null || dataModel.date == null) {
            return;
        }

        if (mSelectDateListener != null) {
            mSelectDateListener.onSelectDate(dataModel.date);
        }
    }
}
