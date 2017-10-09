package com.cc.custom.calender.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cc.custom.R;
import com.cc.custom.TXAbsViewPagerTabFragment;
import com.cc.custom.calender.demo.TXDate;

import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_END_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_START_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_TYPE;
import static com.cc.custom.calender.demo.TXCalendarConst.Type.DAY;
import static com.cc.custom.calender.demo.TXCalendarConst.Type.MONTH;
import static com.cc.custom.calender.demo.TXCalendarConst.Type.WEEK;
import static com.cc.custom.calender.demo.TXCalendarConst.Type.YEAR;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerHomeFragment extends TXAbsViewPagerTabFragment {

    private static final int COUNT = 4;
    private static final int POS_DAY = 0;
    private static final int POS_WEEK = 1;
    private static final int POS_MONTH = 2;
    private static final int POS_YEAR = 3;

    private TXDate mStartDate;
    private TXDate mEndDate;
    private int mType;

    private TXCalendarPickerBaseFragment[] mFragments;

    public static TXCalendarPickerHomeFragment newInstance(int type, TXDate startDate, TXDate endDate) {
        Bundle args = new Bundle();
        args.putInt(INTENT_TYPE, type);
        args.putSerializable(INTENT_START_DATE, startDate);
        args.putSerializable(INTENT_END_DATE, endDate);

        TXCalendarPickerHomeFragment fragment = new TXCalendarPickerHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFragments = new TXCalendarPickerBaseFragment[COUNT];
        mFragments[POS_DAY] = TXCalendarPickerDayFragment.newInstance();
        mFragments[POS_WEEK] = TXCalendarPickerWeekFragment.newInstance();
        mFragments[POS_MONTH] = TXCalendarPickerMonthFragment.newInstance();
        mFragments[POS_YEAR] = TXCalendarPickerYearFragment.newInstance();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mType = arguments.getInt(INTENT_TYPE, DAY);
            mStartDate = (TXDate) arguments.getSerializable(INTENT_START_DATE);
            mEndDate = (TXDate) arguments.getSerializable(INTENT_END_DATE);
        }

        int position;
        switch (mType) {
            case DAY:
                position = POS_DAY;
                mFragments[POS_DAY].setArguments(mStartDate, mEndDate);
                break;
            case WEEK:
                position = POS_WEEK;
                mFragments[WEEK].setArguments(mStartDate, mEndDate);
                break;
            case MONTH:
                position = POS_MONTH;
                mFragments[POS_MONTH].setArguments(mStartDate, mEndDate);
                break;
            case YEAR:
            default:
                mFragments[POS_YEAR].setArguments(mStartDate, mEndDate);
                position = POS_YEAR;
                break;
        }

        mViewPager.setOffscreenPageLimit(COUNT);
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
                return mFragments[POS_DAY];
            case POS_WEEK:
                return mFragments[POS_WEEK];
            case POS_MONTH:
                return mFragments[POS_MONTH];
            case POS_YEAR:
            default:
                return mFragments[POS_YEAR];
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
