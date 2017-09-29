package com.cc.custom.calender.demo;

import android.support.v7.widget.RecyclerView;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalenderViewPoolManager {

    private RecyclerView.RecycledViewPool mViewPool;

    private TXCalenderViewPoolManager() {
        mViewPool = new RecyclerView.RecycledViewPool();
        mViewPool.setMaxRecycledViews(0, 10);
    }

    private static final class InnerHolder {
        private static final TXCalenderViewPoolManager INSTANCE = new TXCalenderViewPoolManager();
    }

    public static TXCalenderViewPoolManager getInstance() {
        return InnerHolder.INSTANCE;
    }

    public RecyclerView.RecycledViewPool getRecyclerViewPool() {
        return mViewPool;
    }
}
