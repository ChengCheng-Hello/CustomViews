package com.cc.custom.calender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.cc.custom.R;
import com.cc.custom.calender.demo.TXCalendarPickerActivity;
import com.cc.custom.calender.demo.TXDate;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_END_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_START_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_TYPE;
import static com.cc.custom.calender.demo.TXCalendarConst.Type;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/25.
 */
public class TXCalenderDemoActivity extends FragmentActivity {

    private static final String TAG = "TXCalenderDemoActivity";

    private TXMonthView monthView;

    private int mType = Type.YEAR;
    private TXDate mStartDate;
    private TXDate mEndDate;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXCalenderDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calender_demo);

        monthView = (TXMonthView) findViewById(R.id.monthView);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.set(2017, 9, 1);

                Calendar selectStartCalendar = Calendar.getInstance();
                selectStartCalendar.set(2017, 9, 9);
                Calendar selectEndCalendar = Calendar.getInstance();
                selectEndCalendar.set(2017, 9, 15);

                monthView.showData(calendar, selectStartCalendar, selectEndCalendar);
            }
        });

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXCalenderListDemoActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartDate == null) {
                    Calendar selectStartCalendar = Calendar.getInstance();
                    selectStartCalendar.set(2017, 9, 9);
                    Calendar selectEndCalendar = Calendar.getInstance();
                    selectEndCalendar.set(2017, 9, 15);

                    mStartDate = new TXDate(selectStartCalendar.getTimeInMillis());
                    mEndDate = new TXDate(selectEndCalendar.getTimeInMillis());
                }

                TXCalendarPickerActivity.launchForResult(TXCalenderDemoActivity.this, mType, mStartDate, mEndDate,
                    1001);
            }
        });

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setDate(new Date().getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                if (data == null) {
                    return;
                }

                mType = data.getIntExtra(INTENT_TYPE, Type.DAY);
                mStartDate = (TXDate) data.getSerializableExtra(INTENT_START_DATE);
                mEndDate = (TXDate) data.getSerializableExtra(INTENT_END_DATE);
                if (mStartDate == null || mEndDate == null) {
                    return;
                }

                Log.d(TAG,
                    "type " + mType + ", startDate " + mStartDate.formatYYYYMDHHMM() + " , "
                        + mStartDate.getMilliseconds() + ", endDate " + mEndDate.formatYYYYMDHHMM() + ", "
                        + mEndDate.getMilliseconds());
            }
        }
    }
}
