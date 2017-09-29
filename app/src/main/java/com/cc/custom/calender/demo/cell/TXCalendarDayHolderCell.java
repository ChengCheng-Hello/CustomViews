package com.cc.custom.calender.demo.cell;

import android.view.View;

import com.cc.custom.R;
import com.cc.custom.calender.demo.model.TXMonthModel;
import com.tx.listview.base.cell.TXBaseListCell;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalendarDayHolderCell implements TXBaseListCell<TXMonthModel> {

    public TXCalendarDayHolderCell() {
    }

    @Override
    public void setData(TXMonthModel model) {
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_calendar_day_placeholder;
    }

    @Override
    public void initCellViews(View view) {
    }
}
