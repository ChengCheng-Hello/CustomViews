package com.cc.custom.calender.demo.model;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXYearModel extends TXCalendarModel {

    public List<TXMonthModel> monthList;

    public TXYearModel(TXDayModel date) {
        super(date);
    }

    @Override
    public String toString() {
        return "TXYearModel{" + "monthList=" + monthList + ", dateModel=" + dateModel + '}';
    }
}
