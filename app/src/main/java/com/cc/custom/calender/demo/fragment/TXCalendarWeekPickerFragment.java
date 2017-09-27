package com.cc.custom.calender.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.custom.R;
import com.cc.custom.TXBaseFragment;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarWeekPickerFragment extends TXBaseFragment {

    public static TXCalendarWeekPickerFragment newInstance() {

        Bundle args = new Bundle();

        TXCalendarWeekPickerFragment fragment = new TXCalendarWeekPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_day_picker, container, false);
    }

}
