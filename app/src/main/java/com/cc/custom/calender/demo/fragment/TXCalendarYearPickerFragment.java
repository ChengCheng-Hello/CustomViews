package com.cc.custom.calender.demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.custom.R;
import com.cc.custom.TXBaseFragment;
import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalendarPickerPresenter;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.cell.TXCalendarYearCell;
import com.cc.custom.calender.demo.listener.TXOnGetSelectedDateListener;
import com.cc.custom.calender.demo.listener.TXOnSelectDateRangeListener;
import com.tx.listview.TXListView;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

import java.util.List;

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
public class TXCalendarYearPickerFragment extends TXBaseFragment implements TXCalenderPickerContract.View,
    TXOnCreateCellListener<TXDate>, TXOnGetSelectedDateListener, TXOnSelectDateRangeListener {

    private TXCalenderPickerContract.Presenter mPresenter;

    private TXListView<TXDate> listView;

    public static TXCalendarYearPickerFragment newInstance(TXDate startDate, TXDate endDate) {
        // TODO: 2017/9/27 null

        Bundle args = new Bundle();
        args.putSerializable(INTENT_START_DATE, startDate);
        args.putSerializable(INTENT_END_DATE, endDate);

        TXCalendarYearPickerFragment fragment = new TXCalendarYearPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calendar_year_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (TXListView<TXDate>) view.findViewById(R.id.listView);

        listView.setOnCreateCellListener(this);

        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }

        TXDate startDate = (TXDate) arguments.getSerializable(INTENT_START_DATE);
        TXDate endDate = (TXDate) arguments.getSerializable(INTENT_END_DATE);

        // TODO null
        if (startDate == null || endDate == null) {
            return;
        }

        new TXCalendarPickerPresenter(this);
        mPresenter.loadDates(TXCalendarConst.Type.YEAR, startDate, endDate);
    }

    @Override
    public void setPresenter(TXCalenderPickerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showDates(List<TXDate> dates, TXDate selectedStartDate, TXDate selectedEndDate) {
        listView.setAllData(dates);
    }

    @Override
    public void showScrollToTopDate(TXDate showTopDate) {
        listView.scrollToData(showTopDate);
    }

    @Override
    public void showSelectCompleted(@TXCalendarConst.Type.TYPE int type, TXDate selectedStartDate,
        TXDate selectedEndDate) {
        if (getActivity() == null) {
            return;
        }

        Intent data = new Intent();
        data.putExtra(INTENT_TYPE, TXCalendarConst.Type.YEAR);
        data.putExtra(INTENT_START_DATE, selectedStartDate);
        data.putExtra(INTENT_END_DATE, selectedEndDate);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    @Override
    public TXBaseListCell<TXDate> onCreateCell(int i) {
        return new TXCalendarYearCell(this, this);
    }

    @Override
    public TXDate getStartDate() {
        return mPresenter.getStartDate();
    }

    @Override
    public TXDate getEndDate() {
        return mPresenter.getEndDate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    @Override
    public void onSelectRange(TXDate startDate, TXDate endDate) {
        mPresenter.selectDateRange(startDate, endDate);
    }
}
