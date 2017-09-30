package com.cc.custom.calender.demo.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarMonthCell;
import com.cc.custom.calender.demo.cell.TXCalendarMonthHolderCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarYearModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerMonthPresenter;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

import static com.cc.custom.calender.demo.TXCalendarConst.ShowType.TYPE_NORMAL;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerMonthFragment extends TXCalendarPickerBaseFragment<TXCalendarYearModel>
    implements TXCalenderPickerContract.View<TXCalendarYearModel>, TXOnCreateCellListener<TXCalendarYearModel>, TXOnSelectDateListener {

    public static TXCalendarPickerMonthFragment newInstance() {
        return new TXCalendarPickerMonthFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerMonthPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.MONTH;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                listView.setPaddingBottom(mScreenHeight - locations[1] - 48 * 3 * 4);
            }
        });
    }

    @Override
    public TXBaseListCell<TXCalendarYearModel> onCreateCell(int type) {
        if (type == TYPE_NORMAL) {
            return new TXCalendarMonthCell(this);
        } else {
            return new TXCalendarMonthHolderCell();
        }
    }
}
