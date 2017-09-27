package com.cc.custom.calender.demo;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TXCalendarConst {

    public static final String INTENT_START_DATE = "intent.start.date";
    public static final String INTENT_END_DATE = "intent.end.date";
    public static final String INTENT_TYPE = "intent.type";

    /**
     * 类型
     */
    public static class Type {
        // 日
        public static final int DAY = 0;
        // 周
        public static final int WEEK = 1;
        // 月
        public static final int MONTH = 2;
        // 年
        public static final int YEAR = 3;

        @IntDef({ DAY, WEEK, MONTH, YEAR })
        @Retention(RetentionPolicy.SOURCE)
        public @interface TYPE {
        }
    }
}
