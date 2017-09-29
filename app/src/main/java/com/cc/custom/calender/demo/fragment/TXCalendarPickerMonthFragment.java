package com.cc.custom.calender.demo.fragment;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarMonthCell;
import com.cc.custom.calender.demo.cell.TXCalendarMonthHolderCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXYearModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerMonthPresenter;
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
public class TXCalendarPickerMonthFragment extends TXCalendarPickerBaseFragment<TXYearModel>
    implements TXCalenderPickerContract.View<TXYearModel>, TXOnCreateCellListener<TXYearModel>, TXOnSelectDateListener {

    public static TXCalendarPickerMonthFragment newInstance() {
        return new TXCalendarPickerMonthFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerMonthPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.MONTH;
    }

    @Override
    public TXBaseListCell<TXYearModel> onCreateCell(int type) {
        if (type == TYPE_NORMAL) {
            return new TXCalendarMonthCell(this);
        } else {
            return new TXCalendarMonthHolderCell();
        }
    }
}
