package com.cc.custom.calender;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/26.
 */
public class TXCalendarView extends FrameLayout{

    public TXCalendarView(@NonNull Context context) {
        super(context);
    }

    public TXCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TXCalendarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
