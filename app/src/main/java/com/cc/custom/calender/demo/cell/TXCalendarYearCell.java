package com.cc.custom.calender.demo.cell;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.cc.custom.R;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarYearModel;
import com.tx.listview.base.cell.TXBaseListCell;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarYearCell implements TXBaseListCell<TXCalendarYearModel> {

    private TextView mTvYear;
    private View mTodayMarkView;
    private TXOnSelectDateListener mSelectDateRangeListener;

    public TXCalendarYearCell(TXOnSelectDateListener selectDateRangeListener) {
        this.mSelectDateRangeListener = selectDateRangeListener;
    }

    @Override
    public void setData(final TXCalendarYearModel data) {
        if (data == null) {
            return;
        }

        final TXDate model = data.year.day;

        int year = model.getYear();

        mTvYear.setText(String.format("%1$d年", year));

        // selected
        if (data.year.isSelected) {
            mTvYear.setTextColor(Color.BLUE);
        } else {
            mTvYear.setTextColor(Color.BLACK);
        }

        // todayMark
        if (data.year.isShowTodayMark) {
            mTodayMarkView.setVisibility(View.VISIBLE);
        } else {
            mTodayMarkView.setVisibility(View.GONE);
        }

        mTvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDateRangeListener != null) {
                    mSelectDateRangeListener.onSelectDate(model);
                }
            }
        });
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_calendar_year;
    }

    @Override
    public void initCellViews(View view) {
        mTvYear = (TextView) view.findViewById(R.id.tv_year);
        mTodayMarkView = view.findViewById(R.id.today_mark);
    }
}
