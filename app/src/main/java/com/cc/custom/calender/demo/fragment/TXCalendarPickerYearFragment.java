package com.cc.custom.calender.demo.fragment;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarYearCell;
import com.cc.custom.calender.demo.cell.TXCalendarYearHolderCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarYearModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerYearPresenter;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerYearFragment extends TXCalendarPickerBaseFragment<TXCalendarYearModel>
    implements TXCalenderPickerContract.View<TXCalendarYearModel>, TXOnCreateCellListener<TXCalendarYearModel>, TXOnSelectDateListener {

    public static TXCalendarPickerYearFragment newInstance() {
        return new TXCalendarPickerYearFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerYearPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.YEAR;
    }

    @Override
    public TXBaseListCell<TXCalendarYearModel> onCreateCell(int type) {
        if (type == 0) {
            return new TXCalendarYearCell(this);
        } else {
            return new TXCalendarYearHolderCell();
        }
    }
}