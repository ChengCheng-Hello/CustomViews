package com.cc.custom.calender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.cc.custom.R;
import com.cc.custom.calender.cell.TXCalendarMonthCell;
import com.cc.custom.calender.cell.TXCalendarTitleCell;
import com.tx.listview.TXListView;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;
import com.tx.listview.base.listener.TXOnGetCellViewTypeListener;
import com.tx.listview.base.listener.TXOnLoadMoreListener;
import com.tx.listview.base.listener.TXOnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/25.
 */
public class TXCalenderListDemoActivity extends FragmentActivity
    implements TXOnRefreshListener, TXOnLoadMoreListener<TXCalendarModel>, TXOnCreateCellListener<TXCalendarModel>,
    TXOnGetCellViewTypeListener<TXCalendarModel>, TXMonthView.TXOnSelectedListener,
    TXCalendarMonthCell.TXOnGetSelectedRangeListener {

    private TXListView<TXCalendarModel> listView;
    private Calendar mSelectStartCalendar;
    private Calendar mSelectEndCalendar;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXCalenderListDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calender_list_demo);

        listView = (TXListView<TXCalendarModel>) findViewById(R.id.listView);

        listView.setOnRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        listView.setOnCreateCellListener(this);
        listView.setOnGetCellViewTypeListener(this);

        initData();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(2017, 8, 1);

        mSelectStartCalendar = Calendar.getInstance();
        mSelectStartCalendar.set(2017, 9, 2);
        mSelectEndCalendar = Calendar.getInstance();
        mSelectEndCalendar.set(2017, 9, 8);

        List<TXCalendarModel> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            TXCalendarModel modelTitle = new TXCalendarModel();
            modelTitle.year = calendar.get(Calendar.YEAR);
            modelTitle.month = calendar.get(Calendar.MONTH);
            modelTitle.day = calendar.get(Calendar.DAY_OF_MONTH);
            modelTitle.type = 10;
            list.add(modelTitle);

            TXCalendarModel model = new TXCalendarModel();
            model.year = calendar.get(Calendar.YEAR);
            model.month = calendar.get(Calendar.MONTH);
            model.day = calendar.get(Calendar.DAY_OF_MONTH);
            model.type = 20;
            list.add(model);

            calendar.add(Calendar.MONTH, 1);
        }

        listView.setAllData(list);
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore(TXCalendarModel txCalendarModel) {

    }

    @Override
    public TXBaseListCell<TXCalendarModel> onCreateCell(int i) {
        if (i == 10) {
            return new TXCalendarTitleCell();
        } else {
            return new TXCalendarMonthCell(this, this);
        }
    }

    @Override
    public int getCellViewType(TXCalendarModel txCalendarModel) {
        return txCalendarModel.type;
    }

    @Override
    public void onSelected(Calendar startCalendar, Calendar endCalendar) {
        mSelectStartCalendar = (Calendar) startCalendar.clone();
        mSelectEndCalendar = (Calendar) endCalendar.clone();
        listView.notifyDataChanged();
    }

    @Override
    public Calendar onGetStart() {
        return mSelectStartCalendar;
    }

    @Override
    public Calendar onGetEnd() {
        return mSelectEndCalendar;
    }
}
