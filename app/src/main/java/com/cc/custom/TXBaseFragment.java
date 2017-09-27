package com.cc.custom;

import android.support.v4.app.Fragment;

public class TXBaseFragment extends Fragment {
    private static final String TAG = TXBaseFragment.class.getSimpleName();

    public boolean onBackPressed() {
        return false;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 判断当前fragment是否活跃
     * 
     * @return
     */
    public boolean isActive() {
        return isAdded();
    }
}
