package com.cc.custom.viewpager;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/19.
 */
public class TXScaleSwitchTransformer implements ViewPager.PageTransformer {

    public static final float SCALE_MAX = 0.8F;

    public static final float ALPHA_MAX = 0.7F;

    private float mScale = SCALE_MAX;

    private float mAlpha = ALPHA_MAX;

    public TXScaleSwitchTransformer() {

    }

    public TXScaleSwitchTransformer(float scale, float alpha) {
        this.mScale = scale;
        this.mAlpha = alpha;
    }

    @Override
    public void transformPage(View page, float position) {
        float scale = position < 0 ? (1 - mScale) * position + 1 : (mScale - 1) * position + 1;
        float alpha = position < 0 ? (1 - mAlpha) * position + 1 : (mAlpha - 1) * position + 1;
        // 为了滑动过程中page间距不变，这里做了处理
        if (position < 0) {
            ViewCompat.setPivotX(page, page.getWidth());
            ViewCompat.setPivotY(page, page.getHeight() / 2);
        } else {
            ViewCompat.setPivotX(page, 0);
            ViewCompat.setPivotY(page, page.getHeight() / 2);
        }
        ViewCompat.setScaleX(page, scale);
        ViewCompat.setScaleY(page, scale);
        ViewCompat.setAlpha(page, Math.abs(alpha));
    }
}
