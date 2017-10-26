package com.cc.custom;

import android.content.Context;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/25.
 */
public class TXContextManager {

    private TXContextManager() {
    }

    private static final class InnerHolder {
        private static final TXContextManager INSTANCE = new TXContextManager();
    }

    public static TXContextManager getInstance() {
        return InnerHolder.INSTANCE;
    }

    private Context mContext;

    public Context getApplicationContext() {
        return mContext;
    }

    public void init(Context context) {
        mContext = context;
    }
}
