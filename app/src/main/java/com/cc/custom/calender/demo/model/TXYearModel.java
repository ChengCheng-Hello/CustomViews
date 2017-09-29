package com.cc.custom.calender.demo.model;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXYearModel {

    public List<TXMonthModel> monthList;
    public TXDateModel year;
    public boolean isHolder;

    public TXYearModel(TXDateModel date) {
        this.year = date;
    }

    @Override
    public String toString() {
        return "TXYearModel{" + "monthList=" + monthList + ", year=" + year + '}';
    }
}
