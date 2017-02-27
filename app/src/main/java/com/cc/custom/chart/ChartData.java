package com.cc.custom.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheng on 16/7/11.
 */
public class ChartData {

    private float mYMax = -Float.MAX_VALUE;
    private float mYMin = Float.MAX_VALUE;
    private float mXMin = 0;
    private float mXMax = 0;

    private List<ChartItem> mDataSets;

    public ChartData(List<ChartItem> mDataSets) {
        this.mDataSets = new ArrayList<>();
        init();
    }

    private void init() {
        calcMinMax();
    }

    private void calcMinMax() {
        if (mDataSets == null) {
            return;
        }

        for (int i = 0; i < mDataSets.size(); i++) {
            ChartItem item = mDataSets.get(i);
            if (item.value > mYMax) {
                mYMax = item.value;
            }

            if (item.value < mYMin) {
                mYMin = item.value;
            }
        }
    }


}
