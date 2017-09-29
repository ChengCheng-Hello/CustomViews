package com.cc.custom.calender.demo.presenter;

import android.os.AsyncTask;

import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.model.TXDateModel;
import com.cc.custom.calender.demo.model.TXMonthModel;
import com.cc.custom.calender.demo.model.TXYearModel;

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
public class TXCalendarPickerMonthPresenter implements TXCalenderPickerContract.Presenter {

    private TXCalenderPickerContract.View<TXYearModel> mView;

    // 显示总年数：3
    private static final int YEAR_COUNT = 3;
    // 向前显示年数：2
    private static final int YEAR_OFFSET = 2;
    // 需要显示置顶的日期
    private TXYearModel mShowTopDate;

    public TXCalendarPickerMonthPresenter(TXCalenderPickerContract.View<TXYearModel> view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void loadDates(TXDate startDate, TXDate endDate) {
        AsyncTask<TXDate, Void, Void> task = new AsyncTask<TXDate, Void, Void>() {

            private List<TXYearModel> dates;
            private TXDate endDate;
            private TXDate startDate;

            @Override
            protected Void doInBackground(TXDate...params) {
                startDate = params[0];
                endDate = params[1];

                dates = getYearList(startDate);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mView.showDates(dates, startDate, endDate);
                mView.showScrollToTopDate(mShowTopDate);
            }
        };

        task.execute(startDate, endDate);
    }

    @Override
    public void selectDateRange(TXDate selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedDate.getYear());
        calendar.set(Calendar.MONTH, selectedDate.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        TXDate endDate = new TXDate(calendar.getTimeInMillis());

        mView.showSelectCompleted(selectedDate, endDate);
    }

    /**
     * 获取年数据
     *
     * @param selectedStartDate 选中开始日期
     */
    private List<TXYearModel> getYearList(TXDate selectedStartDate) {
        TXDate today = new TXDate(System.currentTimeMillis());
        int todayYear = today.getYear();
        int todayMonth = today.getMonth();

        int selectedYear = -1;
        int selectedMonth = -1;

        if (selectedStartDate != null) {
            selectedYear = selectedStartDate.getYear();
            selectedMonth = selectedStartDate.getMonth();
        }

        List<TXYearModel> dates = new ArrayList<>(YEAR_COUNT);

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

            TXYearModel yearModel = new TXYearModel(new TXDateModel(new TXDate(calendar.getTimeInMillis())));

            if (selectedYear == -1) {
                // 没有选中日期，置顶日期为当日
                if (calendarYear == todayYear) {
                    mShowTopDate = yearModel;
                }
            } else {
                // 有选中日期，置顶日期为选中日期
                if (calendarYear == selectedYear) {
                    mShowTopDate = yearModel;
                }
            }

            // monthList
            yearModel.monthList = getMonthList(calendarYear, todayYear, todayMonth, selectedYear, selectedMonth);

            dates.add(yearModel);
        }

        return dates;
    }

    /**
     * 获取月数据
     *
     * @param year 年份
     * @param todayYear 今年
     * @param todayMonth 今月
     * @param selectedYear 选中年
     * @param selectedMonth 选中月
     */
    private List<TXMonthModel> getMonthList(int year, int todayYear, int todayMonth, int selectedYear,
        int selectedMonth) {

        List<TXMonthModel> monthList = new ArrayList<>(12);

        for (int i = 0; i < 12; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            TXDateModel dateModel = new TXDateModel(new TXDate(calendar.getTimeInMillis()));
            // 选中月份
            dateModel.isSelected = selectedYear == year && selectedMonth == i;
            // 当日标志
            dateModel.isShowTodayMark = year == todayYear && i == todayMonth;

            TXMonthModel monthModel = new TXMonthModel(dateModel);

            monthList.add(monthModel);
        }

        return monthList;
    }

    @Override
    public void destroy() {
    }
}
