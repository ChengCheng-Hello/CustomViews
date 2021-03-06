package com.cc.custom.calender.demo.presenter;

import android.os.AsyncTask;
import android.os.Handler;

import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.model.TXCalendarDayModel;
import com.cc.custom.calender.demo.model.TXCalendarYearModel;

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
public class TXCalendarPickerYearPresenter implements TXCalenderPickerContract.Presenter {

    private TXCalenderPickerContract.View<TXCalendarYearModel> mView;

    // 显示总年数：6
    private static final int YEAR_COUNT = 6;
    // 向前显示年数：5
    private static final int YEAR_OFFSET = 5;
    // 需要显示置顶的日期
    private TXCalendarYearModel mShowTopDate;

    public TXCalendarPickerYearPresenter(TXCalenderPickerContract.View<TXCalendarYearModel> view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void init() {
    }

    @Override
    public void loadDates(TXDate startDate, TXDate endDate) {
        AsyncTask<TXDate, Void, Void> task = new AsyncTask<TXDate, Void, Void>() {

            private List<TXCalendarYearModel> dates;

            @Override
            protected Void doInBackground(TXDate...params) {
                dates = getYearList(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mView.showDates(dates, mShowTopDate);
            }
        };

        task.execute(startDate);

    }

    @Override
    public void selectDate(TXDate selectedDate) {
        AsyncTask<TXDate, Void, TXDate> task = new AsyncTask<TXDate, Void, TXDate>() {

            private List<TXCalendarYearModel> dates;

            @Override
            protected TXDate doInBackground(TXDate...params) {
                dates = getYearList(params[0]);
                return params[0];
            }

            @Override
            protected void onPostExecute(final TXDate selectedDate) {
                mView.showDates(dates, null);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selectedDate.getMilliseconds());
                calendar.set(Calendar.YEAR, selectedDate.getYear() + 1);

                final TXDate endDate = new TXDate(calendar.getTimeInMillis() - 1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mView.showSelectCompleted(selectedDate, endDate);
                    }
                }, 200);
            }
        };

        task.execute(selectedDate);
    }

    /**
     * 获取年数据
     * 
     * @param selectedStartDate 选中开始日期
     */
    private List<TXCalendarYearModel> getYearList(TXDate selectedStartDate) {
        TXDate today = new TXDate(System.currentTimeMillis());
        int todayYear = today.getYear();

        int selectedYear = -1;
        if (selectedStartDate != null) {
            selectedYear = selectedStartDate.getYear();
        }

        List<TXCalendarYearModel> dates = new ArrayList<>(YEAR_COUNT);

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

            TXCalendarDayModel dateModel = new TXCalendarDayModel(new TXDate(calendar.getTimeInMillis()));
            // 选中年份
            dateModel.isSelected = selectedYear == calendarYear;

            TXCalendarYearModel yearModel = new TXCalendarYearModel(dateModel);

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

            yearModel.year.isShowTodayMark = todayYear == calendarYear;
            dates.add(yearModel);
        }

        return dates;
    }

    @Override
    public void destroy() {

    }
}
