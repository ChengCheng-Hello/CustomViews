package com.cc.custom.calender.demo.model;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXMonthModel {

    public List<TXDateModel> dayList;

    public TXDateModel month;

    public int weekCount;

    public int firstDayOffset;

    public int lastDayOfMonth;

    public boolean isHolder;

    public TXMonthModel(TXDateModel date) {
        this.month = date;
    }

    @Override
    public String toString() {
        return "TXMonthModel{" + "dayList=" + dayList + ", month=" + month + '}';
    }
}
