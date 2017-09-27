package com.cc.custom.calender.demo;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarModel {

    private TXDate startDate;
    private TXDate endDate;

    private TXDate selectedStartDate;
    private TXDate selectedEndDate;

    private int type;

    private List<TXDate> dateList;

    public TXCalendarModel(TXDate startDate, TXDate endDate, @TXCalendarConst.Type.TYPE int type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public TXDate getStartDate() {
        return startDate;
    }

    public void setStartDate(TXDate startDate) {
        this.startDate = startDate;
    }

    public TXDate getEndDate() {
        return endDate;
    }

    public void setEndDate(TXDate endDate) {
        this.endDate = endDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TXDate> getDateList() {
        return dateList;
    }

    public void setDateList(List<TXDate> dateList) {
        this.dateList = dateList;
    }
}
