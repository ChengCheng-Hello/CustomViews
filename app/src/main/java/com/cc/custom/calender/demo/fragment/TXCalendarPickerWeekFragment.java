package com.cc.custom.calender.demo.fragment;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarWeekCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXMonthModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerWeekPresenter;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerWeekFragment extends TXCalendarPickerBaseFragment<TXMonthModel> implements
    TXCalenderPickerContract.View<TXMonthModel>, TXOnCreateCellListener<TXMonthModel>, TXOnSelectDateListener {

    public static TXCalendarPickerWeekFragment newInstance() {
        return new TXCalendarPickerWeekFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerWeekPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.WEEK;
    }

    @Override
    public TXBaseListCell<TXMonthModel> onCreateCell(int i) {
        return new TXCalendarWeekCell(this);
    }
}
