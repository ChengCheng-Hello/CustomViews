package com.cc.custom.calender.demo.fragment;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarWeekCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarMonthModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerWeekPresenter;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerWeekFragment extends TXCalendarPickerBaseFragment<TXCalendarMonthModel>
    implements TXCalenderPickerContract.View<TXCalendarMonthModel>, TXOnCreateCellListener<TXCalendarMonthModel>,
    TXOnSelectDateListener {

    public static TXCalendarPickerWeekFragment newInstance() {
        return new TXCalendarPickerWeekFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerWeekPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.WEEK;
    }

    @Override
    public void showDates(final List<TXCalendarMonthModel> dates, final TXCalendarMonthModel showTopDate) {
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int[] locations = new int[2];
                listView.getLocationOnScreen(locations);

                int[] locations2 = new int[2];
                listView.getLocationInWindow(locations2);

                int a = 10;

                DisplayMetrics dm = getResources().getDisplayMetrics();
                int mScreenHeight = dm.heightPixels;

                TXCalendarMonthModel model = dates.get(dates.size() - 1);

                listView.setPaddingBottom(mScreenHeight - locations[1] - 48 * 3 - 48 * 3 * model.weekCount);

                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAllData(dates);
                        listView.scrollToData(showTopDate);
                    }
                });
            }
        });

    }

    @Override
    public TXBaseListCell<TXCalendarMonthModel> onCreateCell(int i) {
        return new TXCalendarWeekCell(this);
    }
}
