package com.cc.custom.calender.demo.model;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalendarYearModel extends TXCalendarModel {

    public List<TXCalendarMonthModel> monthList;
    public TXCalendarDayModel year;

    public TXCalendarYearModel(TXCalendarDayModel date) {
        super();
        year = date;
    }

    @Override
    public String toString() {
        return "TXCalendarYearModel{" + "monthList=" + monthList + ", month=" + year + '}';
    }
}
