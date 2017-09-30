package com.cc.custom.calender.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cc.custom.R;
import com.cc.custom.calender.demo.fragment.TXCalendarPickerHomeFragment;

import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_END_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_START_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_TYPE;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerActivity extends FragmentActivity {

    private static final String TAG = "TXCalendarPickerActivity";

    public static void launchForResult(Activity activity, @TXCalendarConst.Type.TYPE int type, TXDate startDate,
        TXDate endDate, int requestCode) {

        Intent intent = new Intent(activity, TXCalendarPickerActivity.class);
        intent.putExtra(INTENT_TYPE, type);
        intent.putExtra(INTENT_START_DATE, startDate);
        intent.putExtra(INTENT_END_DATE, endDate);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calender_picker);

        int type = getIntent().getIntExtra(INTENT_TYPE, TXCalendarConst.Type.DAY);
        TXDate startDate = (TXDate) getIntent().getSerializableExtra(INTENT_START_DATE);
        TXDate endDate = (TXDate) getIntent().getSerializableExtra(INTENT_END_DATE);

        // TODO null

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = TXCalendarPickerHomeFragment.newInstance(type, startDate, endDate);
            fm.beginTransaction().add(R.id.fl_content, fragment, TAG).commitAllowingStateLoss();
        }
    }

}
