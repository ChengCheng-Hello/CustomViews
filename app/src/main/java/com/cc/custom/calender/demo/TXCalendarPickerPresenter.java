package com.cc.custom.calender.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerPresenter implements TXCalenderPickerContract.Presenter {

    private TXCalenderPickerContract.View mView;

    private static final int YEAR_COUNT = 30;
    private static final int YEAR_OFFSET = 4;

    @TXCalendarConst.Type.TYPE
    private int mType;
    private TXDate mStartDate;
    private TXDate mEndDate;

    private TXDate mShowTopDate;

    public TXCalendarPickerPresenter(TXCalenderPickerContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void loadDates(@TXCalendarConst.Type.TYPE int type, TXDate startDate, TXDate endDate) {
        mType = type;
        mStartDate = startDate;
        mEndDate = endDate;

        List<TXDate> dates;

        switch (type) {
            case TXCalendarConst.Type.YEAR:
            default:
                dates = getYearDates(startDate, endDate);
                break;

        }

        mView.showDates(dates, startDate, endDate);
        mView.showScrollToTopDate(mShowTopDate);
    }

    @Override
    public void selectDateRange(TXDate selectedStartDate, TXDate selectedEndDate) {
        mView.showSelectCompleted(mType, selectedStartDate, selectedEndDate);
    }

    @Override
    public TXDate getStartDate() {
        return mStartDate;
    }

    @Override
    public TXDate getEndDate() {
        return mEndDate;
    }

    private List<TXDate> getYearDates(TXDate startDate, TXDate endDate) {
        TXDate today = new TXDate(System.currentTimeMillis());
        int todayYear = today.getYear();

        int year = startDate.getYear();

        List<TXDate> dates = new ArrayList<>(YEAR_COUNT);

        for (int i = 0; i < YEAR_COUNT; i++) {
            Calendar calendar = Calendar.getInstance();
            int calendarYear = todayYear - YEAR_OFFSET + i;
            calendar.set(Calendar.YEAR, calendarYear);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            TXDate date = new TXDate(calendar.getTimeInMillis());

            if (calendarYear == year) {
                mShowTopDate = date;
            }
            dates.add(date);
        }

        return dates;
    }

}
