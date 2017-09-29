package com.cc.custom.calender.demo.model;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/29.
 */
public class TXCalendarModel {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOLDER = 1;

    public int type;

    public TXDayModel dateModel;

    public TXCalendarModel(TXDayModel date) {
        this.dateModel = date;
        this.type = TYPE_NORMAL;
    }
}
