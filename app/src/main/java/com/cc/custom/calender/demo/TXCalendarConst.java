package com.cc.custom.calender.demo;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TXCalendarConst {

    public static final String INTENT_START_DATE = "intent.start.day";
    public static final String INTENT_END_DATE = "intent.end.day";
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

    /**
     * 显示类型
     */
    public static class ShowType {
        // 普通数据
        public static final int TYPE_NORMAL = 0;
        // 占位数据
        public static final int TYPE_HOLDER = 1;

        @IntDef({ TYPE_NORMAL, TYPE_HOLDER })
        @Retention(RetentionPolicy.SOURCE)
        public @interface TYPE {
        }
    }
}
