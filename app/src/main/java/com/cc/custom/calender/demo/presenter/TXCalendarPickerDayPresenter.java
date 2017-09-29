package com.cc.custom.calender.demo.presenter;

import android.os.AsyncTask;

import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.model.TXCalendarDayModel;
import com.cc.custom.calender.demo.model.TXCalendarMonthModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.cc.custom.calender.demo.TXCalendarConst.ShowType.TYPE_HOLDER;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerDayPresenter implements TXCalenderPickerContract.Presenter {

    private TXCalenderPickerContract.View<TXCalendarMonthModel> mView;

    // 向前2年对应月数
    private static final int MONTH_OFFSET = 24;
    // 需要显示置顶的日期
    private TXCalendarMonthModel mShowTopDate;

    public TXCalendarPickerDayPresenter(TXCalenderPickerContract.View<TXCalendarMonthModel> view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void init() {
    }

    @Override
    public void loadDates(TXDate startDate, TXDate endDate) {
        AsyncTask<TXDate, Void, Void> task = new AsyncTask<TXDate, Void, Void>() {

            private List<TXCalendarMonthModel> dates;

            @Override
            protected Void doInBackground(TXDate...params) {
                dates = getMonthList(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mView.showDates(dates);
                mView.showScrollToTopDate(mShowTopDate);
            }
        };

        task.execute(startDate);
    }

    @Override
    public void selectDate(TXDate selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedDate.getYear());
        calendar.set(Calendar.MONTH, selectedDate.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, selectedDate.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        TXDate endDate = new TXDate(calendar.getTimeInMillis());

        mView.showSelectCompleted(selectedDate, endDate);
    }

    /**
     * 获取月数据
     *
     * @param selectedStartDate 选中开始日期
     */
    private List<TXCalendarMonthModel> getMonthList(TXDate selectedStartDate) {
        TXDate today = new TXDate(System.currentTimeMillis());
        int todayYear = today.getYear();
        int todayMonth = today.getMonth();
        int todayDay = today.getDay();
        int monthCount = MONTH_OFFSET + 1;

        int selectedYear = -1;
        int selectedMonth = -1;
        int selectedDay = -1;

        if (selectedStartDate != null) {
            selectedYear = selectedStartDate.getYear();
            selectedMonth = selectedStartDate.getMonth();
            selectedDay = selectedStartDate.getDay();
        }

        List<TXCalendarMonthModel> dates = new ArrayList<>(monthCount);

        for (int i = 0; i < monthCount; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, todayYear);
            calendar.set(Calendar.MONTH, todayMonth - MONTH_OFFSET + i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            TXCalendarMonthModel monthModel = new TXCalendarMonthModel(new TXCalendarDayModel(new TXDate(calendar.getTimeInMillis())));

            int calendarYear = calendar.get(Calendar.YEAR);
            int calendarMonth = calendar.get(Calendar.MONTH);
            if (selectedYear == -1) {
                // 没有选中日期，置顶日期为当日
                if (calendarYear == todayYear && calendarMonth == todayMonth) {
                    mShowTopDate = monthModel;
                }
            } else {
                // 有选中日期，置顶日期为选中日期
                if (calendarYear == selectedYear && calendarMonth == selectedMonth) {
                    mShowTopDate = monthModel;
                }
            }

            // firstDayOffset
            int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            monthModel.firstDayOffset = getWeek(firstDayOfWeek) - 1;
            monthModel.lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            // weekCount
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            monthModel.weekCount = calendar.get(Calendar.WEEK_OF_MONTH);

            // dayList
            int dayCount = monthModel.weekCount * 7;
            monthModel.dayList =
                getDayList(calendarYear, calendarMonth, todayYear, todayMonth, todayDay, monthModel.firstDayOffset,
                    monthModel.lastDayOfMonth, dayCount, selectedYear, selectedMonth, selectedDay);

            dates.add(monthModel);
        }

        TXCalendarMonthModel monthModel = new TXCalendarMonthModel(null);
        monthModel.type = TYPE_HOLDER;
        dates.add(monthModel);

        return dates;
    }

    private List<TXCalendarDayModel> getDayList(int year, int month, int todayYear, int todayMonth, int todayDay,
                                                int firstDayOffset, int lastDayOfMonth, int dayCount, int selectedYear, int selectedMonth, int selectedDay) {

        List<TXCalendarDayModel> dayList = new ArrayList<>(dayCount);

        for (int i = 0; i < dayCount; i++) {
            if (i < firstDayOffset || i >= lastDayOfMonth + firstDayOffset) {
                TXCalendarDayModel model = new TXCalendarDayModel(null);
                dayList.add(model);
                continue;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            int day = i - firstDayOffset + 1;
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            TXCalendarDayModel dateModel = new TXCalendarDayModel(new TXDate(calendar.getTimeInMillis()));
            // 选中日期
            dateModel.isSelected = selectedYear == year && selectedMonth == month && selectedDay == day;
            // 当日标志
            dateModel.isShowTodayMark = year == todayYear && month == todayMonth && day == todayDay;

            dayList.add(dateModel);
        }

        return dayList;
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

    @Override
    public void destroy() {
    }
}
