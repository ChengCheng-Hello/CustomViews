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

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * 月日视图（单日／周模式）
 * <p>
 * Created by Cheng on 2017/9/25.
 */
public class TXMonthView extends View {

    private Calendar mShowMonth;
    private Calendar mSelectDay;

    private int mWeekOfMonth;
    private int mLastDayOfMonth;
    private int mFirstDayOfWeek;

    private List<Integer> mSelectDays = new LinkedList<>();

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
    private boolean mShowCurrentDay;
    private Calendar mCurrentCalendar;
    private int mCurrentDay;

    private GestureDetectorCompat mGestureDetector;
    private int mClickDay;
    private TXOnSelectedListener mSelectedListener;
    private int mFirstDayOffset;

    public TXMonthView(Context context) {
        this(context, null);
    }

    public TXMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDayTextColor = Color.BLACK;
        mDayTextSize = 40;

        mDayTextSelectColor = Color.WHITE;
        mDaySelectBgColor = Color.BLUE;

        init();
    }

    public void showData(@NonNull Calendar showMonth, Calendar selectStartCalendar, Calendar selectEndCalendar) {
        showMonth.setFirstDayOfWeek(Calendar.MONDAY);

        mShowMonth = showMonth;
        mSelectDay = (Calendar) mShowMonth.clone();

        calcFields();

        if (selectStartCalendar != null && selectEndCalendar != null) {
            selectStartCalendar.setFirstDayOfWeek(Calendar.MONDAY);
            selectEndCalendar.setFirstDayOfWeek(Calendar.MONDAY);
            if (selectStartCalendar.get(Calendar.MONTH) == showMonth.get(Calendar.MONTH)) {
                mSelectDay = selectStartCalendar;
                calcSelectDays(selectStartCalendar);
            } else if (selectEndCalendar.get(Calendar.MONTH) == showMonth.get(Calendar.MONTH)) {
                mSelectDay = selectEndCalendar;
                calcSelectDays(selectEndCalendar);
            } else {
                mSelectDays.clear();
            }
        }

        requestLayout();
    }

    public void setOnSelectedListener(TXOnSelectedListener listener) {
        this.mSelectedListener = listener;
    }

    public interface TXOnSelectedListener {
        void onSelected(Calendar startCalendar, Calendar endCalendar);
    }

    public Calendar getSelectDay() {
        return mSelectDay;
    }

    private void calcFields() {
        Calendar calendar = (Calendar) mShowMonth.clone();

        if (mShowMonth.get(Calendar.YEAR) == mCurrentCalendar.get(Calendar.YEAR)
            && mShowMonth.get(Calendar.MONTH) == mCurrentCalendar.get(Calendar.MONTH)) {
            mShowCurrentDay = true;
        } else {
            mShowCurrentDay = false;
        }

        mFirstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mFirstDayOffset = getWeek(mFirstDayOfWeek) - 1;

        mLastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, mLastDayOfMonth);

        mWeekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
    }

    private void calcSelectDays(Calendar calendar) {
        Calendar tempCalendar = (Calendar) calendar.clone();
        int currentDayOfWeek = getWeek(tempCalendar.get(Calendar.DAY_OF_WEEK));
        int offSet = currentDayOfWeek - 1;

        tempCalendar.set(Calendar.DAY_OF_MONTH, tempCalendar.get(Calendar.DAY_OF_MONTH) - offSet);

        mSelectDays.clear();
        for (int i = 0; i < mColumnCount; i++) {
            if (i > 0) {
                tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            if (tempCalendar.get(Calendar.MONTH) != mShowMonth.get(Calendar.MONTH)
                || tempCalendar.get(Calendar.YEAR) != mShowMonth.get(Calendar.YEAR)) {
                continue;
            }

            mSelectDays.add(tempCalendar.get(Calendar.DAY_OF_MONTH));
        }
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

        mCurrentCalendar = Calendar.getInstance(Locale.CHINA);
        mCurrentCalendar.setTime(new Date());
        mCurrentDay = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        mWidth = getWidth();
        int height = mRowHeight * mWeekOfMonth;

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

        int day = 1;

        for (int i = 0; i < mWeekOfMonth; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                if (day > mLastDayOfMonth) {
                    break;
                }
                if (i == 0 && j < mFirstDayOffset) {
                    // 如果是第一行可能不全
                    continue;
                }
                int centerX = mColumnWidth * j + mColumnWidth / 2;
                int centerY = mRowHeight * i + mRowHeight / 2 + mLineWidth * i;

                // text
                String content = String.valueOf(day);
                float textWidth = mDayTextPaint.measureText(content);

                // bg
                if (mSelectDays.contains(day)) {
                    canvas.drawCircle(centerX, centerY, mSelectBgRadius, mDaySelectBgPaint);
                    canvas.drawText(content, centerX - textWidth / 2, centerY + mTextHeight / 2, mDayTextSelectPaint);
                } else {
                    canvas.drawText(content, centerX - textWidth / 2, centerY + mTextHeight / 2, mDayTextPaint);
                }

                if (mShowCurrentDay && mCurrentDay == day) {
                    canvas.drawPoint(centerX, centerY + mTextHeight + 6, mCurrentDayPaint);
                }

                day++;
            }

            if (i < mWeekOfMonth - 1) {
                int lineY = mRowHeight * (i + 1);
                canvas.drawLine(0, mRowHeight * (i + 1), mWidth, lineY, mLinePaint);
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
        int rowIndex = mWeekOfMonth;
        for (int i = 0; i < mWeekOfMonth; i++) {
            if (y > (rowIndex - 1) * mRowHeight) {
                break;
            }
            rowIndex--;
        }

        int clickDay = colIndex + (rowIndex - 1) * mColumnCount - mFirstDayOffset;
        if (clickDay <= 0 || clickDay > mLastDayOfMonth || clickDay == mClickDay) {
            return;
        }
        mClickDay = clickDay;
        mSelectDay.set(Calendar.DAY_OF_MONTH, mClickDay);
        calcSelectDays(mSelectDay);

        if (mSelectedListener != null) {
            Calendar start = (Calendar) mSelectDay.clone();
            start.set(Calendar.DAY_OF_MONTH, mSelectDays.get(0));
            int week = getWeek(start.get(Calendar.DAY_OF_WEEK));
            start.add(Calendar.DAY_OF_MONTH, -week + 1);

            Calendar end = (Calendar) start.clone();
            end.add(Calendar.DAY_OF_MONTH, mColumnCount - 1);
            mSelectedListener.onSelected(start, end);
        }

        invalidate();
    }

    public static String formatWeek(Calendar c) {
        int week = c.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            case Calendar.SUNDAY:
            default:
                return "星期日";
        }
    }

    public int getWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            case Calendar.SUNDAY:
            default:
                return 7;
        }
    }
}
