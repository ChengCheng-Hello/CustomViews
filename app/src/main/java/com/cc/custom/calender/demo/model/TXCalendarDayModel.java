package com.cc.custom.calender.demo.model;

import com.cc.custom.calender.demo.TXDate;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalendarDayModel extends TXCalendarModel {

    public TXDate day;

    public boolean isSelected;

    public boolean isShowTodayMark;

    public TXCalendarDayModel(TXDate date) {
        super();
        this.day = date;
    }

    @Override
    public String toString() {
        return "TXCalendarDayModel{" + "day=" + day + ", isSelected=" + isSelected + '}';
    }
}
