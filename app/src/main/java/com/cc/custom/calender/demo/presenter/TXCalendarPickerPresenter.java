package com.cc.custom.calender.demo.presenter;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.TXDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.type;

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
    private static final int MONTH_COUNT = 30;
    private static final int YEAR_OFFSET = 4;
    private static final int MONTH_OFFSET = 10;

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
    public void loadDates(TXDate startDate, TXDate endDate) {
        mStartDate = startDate;
        mEndDate = endDate;

        List<TXDate> dates;

        switch (type) {
            case TXCalendarConst.Type.YEAR:
            default:
                dates = getYearDates(startDate);
                break;

        }

        mView.showDates(dates, startDate, endDate);
        mView.showScrollToTopDate(mShowTopDate);
    }

    @Override
    public void selectDateRange(TXDate selectedStartDate) {
        mView.showSelectCompleted(selectedStartDate, null);
    }

    /**
     * 获取年数据
     * 
     * @param startDate 开始日期
     */
    private List<TXDate> getYearDates(TXDate startDate) {
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

    /**
     * 获取月数据
     *
     * @param startDate 开始日期
     */
    private List<TXDate> getMonthDates(TXDate startDate) {
        TXDate today = new TXDate(System.currentTimeMillis());
        int todayYear = today.getYear();
        int todayMonth = today.getMonth();

        int year = startDate.getYear();
        int month = startDate.getMonth();

        List<TXDate> dates = new ArrayList<>(MONTH_COUNT);

        for (int i = 0; i < MONTH_COUNT; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, todayYear);
            int calendarMonth = todayMonth - MONTH_OFFSET + i;
            calendar.set(Calendar.MONTH, calendarMonth);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            int calendarYear = calendar.get(Calendar.YEAR);

            TXDate date = new TXDate(calendar.getTimeInMillis());

            if (month == calendarMonth || year == calendarYear) {
                mShowTopDate = date;
            }
            dates.add(date);
        }

        return dates;
    }

}
