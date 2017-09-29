package com.cc.custom.calender.demo.cell;

import android.view.View;

import com.cc.custom.R;
import com.cc.custom.calender.demo.model.TXYearModel;
import com.tx.listview.base.cell.TXBaseListCell;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarYearHolderCell implements TXBaseListCell<TXYearModel> {

    @Override
    public void setData(final TXYearModel data) {
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_calendar_year_placeholder;
    }

    @Override
    public void initCellViews(View view) {
    }
}
