package com.cc.custom.calender.demo.model;

import com.cc.custom.calender.demo.TXCalendarConst;

import static com.cc.custom.calender.demo.TXCalendarConst.ShowType.TYPE_NORMAL;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/29.
 */
public class TXCalendarModel {

    @TXCalendarConst.ShowType.TYPE
    public int type;

    public TXCalendarModel() {
        this.type = TYPE_NORMAL;
    }
}
