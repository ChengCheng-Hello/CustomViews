package com.cc.custom.calender.demo.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarDayCell;
import com.cc.custom.calender.demo.cell.TXCalendarDayHolderCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarMonthModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerDayPresenter;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

import java.util.List;

import static com.cc.custom.calender.demo.TXCalendarConst.ShowType.TYPE_NORMAL;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerDayFragment extends TXCalendarPickerBaseFragment<TXCalendarMonthModel> implements
    TXCalenderPickerContract.View<TXCalendarMonthModel>, TXOnCreateCellListener<TXCalendarMonthModel>, TXOnSelectDateListener {

    public static TXCalendarPickerDayFragment newInstance() {
        return new TXCalendarPickerDayFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerDayPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.DAY;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public TXBaseListCell<TXCalendarMonthModel> onCreateCell(int type) {
        if (type == TYPE_NORMAL) {
            return new TXCalendarDayCell(this);
        } else {
            return new TXCalendarDayHolderCell();
        }
    }

    @Override
    public void showDates(final List<TXCalendarMonthModel> dates) {
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
                int  mScreenHeight = dm.heightPixels;

                TXCalendarMonthModel model = dates.get(dates.size() - 1);

                listView.setPaddingBottom(mScreenHeight - locations[1] - 48 * 3  - 48 * 3 * model.weekCount);
            }
        });

        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setAllData(dates);
            }
        });
    }

    @Override
    public void showScrollToTopDate(final TXCalendarMonthModel showTopDate) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.scrollToData(showTopDate);
            }
        });
    }
}
