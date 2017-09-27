package com.cc.custom.calender.demo.cell;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.cc.custom.R;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.listener.TXOnGetSelectedDateListener;
import com.cc.custom.calender.demo.listener.TXOnSelectDateRangeListener;
import com.tx.listview.base.cell.TXBaseListCell;

import java.util.Calendar;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarYearCell implements TXBaseListCell<TXDate> {

    private TextView mTvYear;
    private TXOnGetSelectedDateListener mSelectedListener;
    private TXOnSelectDateRangeListener mSelectDateRangeListener;

    public TXCalendarYearCell(TXOnGetSelectedDateListener getSelectedDateListener,
        TXOnSelectDateRangeListener selectDateRangeListener) {
        this.mSelectedListener = getSelectedDateListener;
        this.mSelectDateRangeListener = selectDateRangeListener;
    }

    @Override
    public void setData(final TXDate model) {
        if (model == null) {
            return;
        }

        int year = model.getYear();
        mTvYear.setText(String.format("%1$d年", year));

        // selected
        if (mSelectedListener != null) {
            TXDate startDate = mSelectedListener.getStartDate();
            if (startDate.getYear() == year) {
                mTvYear.setTextColor(Color.BLUE);
            } else {
                mTvYear.setTextColor(Color.BLACK);
            }
        }

        mTvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDateRangeListener != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, model.getYear());
                    calendar.set(Calendar.MONTH, 11);
                    calendar.set(Calendar.DAY_OF_MONTH, 31);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    calendar.set(Calendar.MILLISECOND, 0);

                    TXDate endDate = new TXDate(calendar.getTimeInMillis());

                    mSelectDateRangeListener.onSelectRange(model, endDate);
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
    }
}
