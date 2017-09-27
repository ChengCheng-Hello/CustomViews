package com.cc.custom.calender.cell;

import android.view.View;
import android.widget.TextView;

import com.cc.custom.R;
import com.cc.custom.calender.TXCalendarModel;
import com.tx.listview.base.cell.TXBaseListCell;

import java.util.Locale;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/26.
 */
public class TXCalendarTitleCell implements TXBaseListCell<TXCalendarModel> {

    private TextView mTvContent;

    @Override
    public void setData(TXCalendarModel model) {
        if (model == null) {
            return;
        }
        mTvContent.setText(String.format(Locale.CHINA, "%1$d年%2$d月", model.year, model.month + 1));
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.item_calendar_title;
    }

    @Override
    public void initCellViews(View view) {
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
    }
}
