package com.cc.custom.calender.demo.fragment;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarDayCell;
import com.cc.custom.calender.demo.cell.TXCalendarDayHolderCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXMonthModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerDayPresenter;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

import static com.cc.custom.calender.demo.model.TXCalendarModel.TYPE_NORMAL;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerDayFragment extends TXCalendarPickerBaseFragment<TXMonthModel> implements
    TXCalenderPickerContract.View<TXMonthModel>, TXOnCreateCellListener<TXMonthModel>, TXOnSelectDateListener {

    public static TXCalendarPickerDayFragment newInstance() {
        return new TXCalendarPickerDayFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerDayPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.DAY;
    }

    @Override
    public TXBaseListCell<TXMonthModel> onCreateCell(int type) {
        if (type == TYPE_NORMAL) {
            return new TXCalendarDayCell(this);
        } else {
            return new TXCalendarDayHolderCell();
        }
    }
}
