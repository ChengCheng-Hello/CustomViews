package com.cc.custom.progress;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/11/15.
 */
public class CurvedAndTiled extends Drawable {

    private final float mCornerRadius;
    private final RectF mRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final Paint mTilePaint;

    CurvedAndTiled(Bitmap bitmap, float cornerRadius) {
        mCornerRadius = cornerRadius;

        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        mTilePaint = new Paint();
        mTilePaint.setAntiAlias(true);
        mTilePaint.setShader(mBitmapShader);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mTilePaint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mTilePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mTilePaint.setColorFilter(cf);
    }
}
