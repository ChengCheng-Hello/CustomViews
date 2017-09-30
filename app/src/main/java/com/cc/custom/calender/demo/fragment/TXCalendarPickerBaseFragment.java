package com.cc.custom.calender.demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.custom.R;
import com.cc.custom.TXBaseFragment;
import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarModel;
import com.tx.listview.TXListView;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;
import com.tx.listview.base.listener.TXOnGetCellViewTypeListener;

import java.util.List;

import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_END_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_START_DATE;
import static com.cc.custom.calender.demo.TXCalendarConst.INTENT_TYPE;
import static com.cc.custom.calender.demo.TXCalendarConst.ShowType.TYPE_HOLDER;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public abstract class TXCalendarPickerBaseFragment<T extends TXCalendarModel> extends TXBaseFragment
    implements TXCalenderPickerContract.View<T>, TXOnCreateCellListener<T>, TXOnSelectDateListener,
    TXOnGetCellViewTypeListener<T> {

    private TXCalenderPickerContract.Presenter mPresenter;

    public TXListView<T> listView;

    @LayoutRes
    public int getLayoutId() {
        return R.layout.fragment_calendar_picker_list;
    }

    public abstract void initPresenter();

    @TXCalendarConst.Type.TYPE
    public abstract int getType();

    @Override
    public abstract TXBaseListCell<T> onCreateCell(int type);

    public void setArguments(TXDate startDate, TXDate endDate) {
        Bundle args = new Bundle();
        args.putSerializable(INTENT_START_DATE, startDate);
        args.putSerializable(INTENT_END_DATE, endDate);
        setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (TXListView<T>) view.findViewById(R.id.listView);
        listView.setOnCreateCellListener(this);
        listView.setOnGetCellViewTypeListener(this);

        TXDate startDate = null;
        TXDate endDate = null;

        Bundle arguments = getArguments();
        if (arguments != null) {
            startDate = (TXDate) arguments.getSerializable(INTENT_START_DATE);
            endDate = (TXDate) arguments.getSerializable(INTENT_END_DATE);
        }

        initPresenter();
        mPresenter.loadDates(startDate, endDate);
    }

    @Override
    public void setPresenter(TXCalenderPickerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showDates(List<T> dates, T showTopDate) {
        listView.setAllData(dates);
        listView.scrollToData(showTopDate);
    }

    @Override
    public void showSelectCompleted(TXDate selectedStartDate, TXDate selectedEndDate) {
        if (getActivity() == null) {
            return;
        }

        Intent data = new Intent();
        data.putExtra(INTENT_TYPE, getType());
        data.putExtra(INTENT_START_DATE, selectedStartDate);
        data.putExtra(INTENT_END_DATE, selectedEndDate);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    @Override
    public void onSelectDate(TXDate selectedDate) {
        mPresenter.selectDate(selectedDate);
    }

    @Override
    public int getCellViewType(T data) {
        if (data == null) {
            return TYPE_HOLDER;
        } else {
            return data.type;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
