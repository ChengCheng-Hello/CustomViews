package com.cc.custom.calender.demo.model;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalendarMonthModel extends TXCalendarModel {

    public TXCalendarDayModel month;

    public List<TXCalendarDayModel> dayList;

    public int weekCount;

    public int lastDayOfMonth;

    public TXCalendarMonthModel(TXCalendarDayModel date) {
        super();
        this.month = date;
    }

    @Override
    public String toString() {
        return "TXCalendarMonthModel{" + "dayList=" + dayList + ", month=" + month + '}';
    }
}
