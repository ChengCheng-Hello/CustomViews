package com.cc.custom.calender.demo.model;

import com.cc.custom.calender.demo.TXDate;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXDayModel {

    public TXDate date;

    public boolean isSelected;

    public boolean isShowTodayMark;

    public TXDayModel(TXDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TXDayModel{" + "date=" + date + ", isSelected=" + isSelected + '}';
    }
}
