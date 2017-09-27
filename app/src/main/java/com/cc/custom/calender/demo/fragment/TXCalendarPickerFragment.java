package com.cc.custom.calender.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cc.custom.R;
import com.cc.custom.TXAbsViewPagerTabFragment;
import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXDate;

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
public class TXCalendarPickerFragment extends TXAbsViewPagerTabFragment {

    private static final int COUNT = 4;
    private static final int POS_DAY = 0;
    private static final int POS_WEEK = 1;
    private static final int POS_MONTH = 2;
    private static final int POS_YEAR = 3;
    private TXDate mStartDate;
    private TXDate mEndDate;
    private int mType;

    public static TXCalendarPickerFragment newInstance(int type, TXDate startDate, TXDate endDate) {
        // TODO: 2017/9/27 null

        Bundle args = new Bundle();
        args.putInt(INTENT_TYPE, type);
        args.putSerializable(INTENT_START_DATE, startDate);
        args.putSerializable(INTENT_END_DATE, endDate);

        TXCalendarPickerFragment fragment = new TXCalendarPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }

        mType = arguments.getInt(INTENT_TYPE, TXCalendarConst.Type.DAY);
        mStartDate = (TXDate) arguments.getSerializable(INTENT_START_DATE);
        mEndDate = (TXDate) arguments.getSerializable(INTENT_END_DATE);

        int position;
        switch (mType) {
            case TXCalendarConst.Type.DAY:
                position = POS_DAY;
                break;
            case TXCalendarConst.Type.WEEK:
                position = POS_WEEK;
                break;
            case TXCalendarConst.Type.MONTH:
                position = POS_MONTH;
                break;
            case TXCalendarConst.Type.YEAR:
            default:
                position = POS_YEAR;
                break;
        }

        mViewPager.setCurrentItem(position, false);
    }

    @Override
    protected int getCount() {
        return COUNT;
    }

    @Override
    protected Fragment getFragment(int position) {
        switch (position) {
            case POS_DAY:
                return TXCalendarDayPickerFragment.newInstance();
            case POS_WEEK:
                return TXCalendarDayPickerFragment.newInstance();
            case POS_MONTH:
                return TXCalendarDayPickerFragment.newInstance();
            case POS_YEAR:
            default:
                return TXCalendarYearPickerFragment.newInstance(mStartDate, mEndDate);
        }
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        switch (position) {
            case POS_DAY:
                return getString(R.string.tx_day);
            case POS_WEEK:
                return getString(R.string.tx_week);
            case POS_MONTH:
                return getString(R.string.tx_month);
            case POS_YEAR:
            default:
                return getString(R.string.tx_year);
        }
    }
}
