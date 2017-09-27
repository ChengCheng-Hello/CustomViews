package com.cc.custom.calender.cell;

import android.view.View;

import com.cc.custom.R;
import com.cc.custom.calender.TXCalendarModel;
import com.cc.custom.calender.TXMonthView;
import com.tx.listview.base.cell.TXBaseListCell;

import java.util.Calendar;
import java.util.Locale;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/26.
 */
public class TXCalendarMonthCell implements TXBaseListCell<TXCalendarModel> {

    private TXMonthView monthView;
    private TXMonthView.TXOnSelectedListener listener;
    private TXOnGetSelectedRangeListener selectedRangeListener;

    public TXCalendarMonthCell(TXMonthView.TXOnSelectedListener listener,
        TXOnGetSelectedRangeListener selectedRangeListener) {
        this.listener = listener;
        this.selectedRangeListener = selectedRangeListener;
    }

    @Override
    public void setData(TXCalendarModel model) {
        if (model == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(model.year, model.month, model.day);

        monthView.setOnSelectedListener(listener);
        monthView.showData(calendar, selectedRangeListener.onGetStart(), selectedRangeListener.onGetEnd());
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.item_calendar_month;
    }

    @Override
    public void initCellViews(View view) {
        monthView = (TXMonthView) view.findViewById(R.id.monthView);
    }

    public interface TXOnGetSelectedRangeListener {
        Calendar onGetStart();

        Calendar onGetEnd();
    }
}
