package com.cc.custom.calender.demo.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

import com.cc.custom.calender.demo.TXCalendarConst;
import com.cc.custom.calender.demo.TXCalenderPickerContract;
import com.cc.custom.calender.demo.cell.TXCalendarYearCell;
import com.cc.custom.calender.demo.cell.TXCalendarYearHolderCell;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarYearModel;
import com.cc.custom.calender.demo.presenter.TXCalendarPickerYearPresenter;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCreateCellListener;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalendarPickerYearFragment extends TXCalendarPickerBaseFragment<TXCalendarYearModel>
    implements TXCalenderPickerContract.View<TXCalendarYearModel>, TXOnCreateCellListener<TXCalendarYearModel>, TXOnSelectDateListener {

    public static TXCalendarPickerYearFragment newInstance() {
        return new TXCalendarPickerYearFragment();
    }

    @Override
    public void initPresenter() {
        new TXCalendarPickerYearPresenter(this);
    }

    @Override
    public int getType() {
        return TXCalendarConst.Type.YEAR;
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
                listView.setPaddingBottom(mScreenHeight - locations[1] - 48 * 3);
            }
        });
    }

    @Override
    public TXBaseListCell<TXCalendarYearModel> onCreateCell(int type) {
        if (type == 0) {
            return new TXCalendarYearCell(this);
        } else {
            return new TXCalendarYearHolderCell();
        }
    }
}
