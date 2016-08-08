package com.cc.stepview.chart;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by Cheng on 16/7/15.
 */
public class ChartAnimation extends Animation {

    private int mCenterX;
    private int mCenterY;

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        mCenterX = width / 2;
        mCenterY = height / 2;

        setDuration(1500);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);


        final Matrix matrix = t.getMatrix();
        matrix.setScale(1, interpolatedTime);
        matrix.preTranslate(-mCenterX, - mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
    }
}
