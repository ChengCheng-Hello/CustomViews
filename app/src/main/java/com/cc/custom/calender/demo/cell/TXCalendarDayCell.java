package com.cc.custom.calender.demo.cell;

import android.view.View;
import android.widget.TextView;

import com.cc.custom.R;
import com.cc.custom.calender.TXMonthView2;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXMonthModel;
import com.tx.listview.base.cell.TXBaseListCell;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalendarDayCell implements TXBaseListCell<TXMonthModel> {

    private TextView mTvTitle;
    private TXMonthView2 mMonthView;
    private TXOnSelectDateListener mSelectDateListener;

    public TXCalendarDayCell(TXOnSelectDateListener selectDateListener) {
        this.mSelectDateListener = selectDateListener;
    }

    @Override
    public void setData(TXMonthModel model) {
        if (model == null) {
            return;
        }

        int year = model.dateModel.date.getYear();
        int month = model.dateModel.date.getMonth();

        mTvTitle.setText(String.format("%1$d年%2$d月", year, month + 1));

        mMonthView.setData(model, mSelectDateListener);
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_calendar_day;
    }

    @Override
    public void initCellViews(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mMonthView = (TXMonthView2) view.findViewById(R.id.monthView);
    }
}
