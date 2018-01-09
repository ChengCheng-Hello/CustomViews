package com.cc.custom.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cc.custom.R;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/11/15.
 */
public class TXVideoProgressBar extends View {

    private Paint mFramePaint;
    private Paint mReachedPaint;
    private Paint mUnreachedPaint;

    private RectF mFrameRectF = new RectF(0, 0, 0, 0);
    private RectF mUnReachedRect = new RectF(0, 0, 0, 0);
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);

    private float mFrameLineWidth = 4;
    private float mFrameHeight;
    private float mUnreachedHeight;
    private float mReachedHeight;

    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;
    private int mHeight;

    public TXVideoProgressBar(Context context) {
        this(context, null);
    }

    public TXVideoProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXVideoProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setColor(Color.GRAY);
        mFramePaint.setStrokeWidth(mFrameLineWidth);
        mFramePaint.setStyle(Paint.Style.STROKE);

        mReachedPaint = new Paint();
        mReachedPaint.setAntiAlias(true);
        mReachedPaint.setColor(Color.parseColor("#007BFF"));
        mReachedPaint.setStrokeWidth(mReachedHeight);
        mReachedPaint.setStyle(Paint.Style.FILL);

        mUnreachedPaint = new Paint();
        mUnreachedPaint.setAntiAlias(true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tx_video_progress_tile);
        mReachedHeight = mUnreachedHeight = bitmap.getHeight();
        mFrameHeight = mReachedHeight * 2.3f;
        mHeight = (int) (mUnreachedHeight * 3);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mUnreachedPaint.setShader(shader);

        // LinearGradient linearGradient = new LinearGradient(0, 0, 0, mHeight, Color.parseColor("#1266aa"),
        // Color.parseColor("#007BFF"), Shader.TileMode.CLAMP);
        // mReachedPaint.setShader(linearGradient);
    }

    public void setProgress(int progress) {
        mCurrentProgress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), mHeight);
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float paddingHeight = (mHeight - mFrameHeight) / 2;
        float radius = mFrameHeight / 2;

        // frame
        mFrameRectF.left = mFrameLineWidth / 2;
        mFrameRectF.top = paddingHeight + mFrameLineWidth / 2;
        mFrameRectF.right = getWidth() - mFrameLineWidth / 2;
        mFrameRectF.bottom = paddingHeight + mFrameHeight - mFrameLineWidth / 2;
        canvas.drawRoundRect(mFrameRectF, radius, radius, mFramePaint);

        // unreached
        mUnReachedRect.left = radius - mUnreachedHeight / 2;
        mUnReachedRect.top = paddingHeight + (mFrameHeight - mUnreachedHeight) / 2;
        mUnReachedRect.right = getWidth() - radius + mUnreachedHeight / 2;
        mUnReachedRect.bottom = paddingHeight + (mFrameHeight + mUnreachedHeight) / 2;
        canvas.drawRoundRect(mUnReachedRect, mUnreachedHeight / 2, mUnreachedHeight / 2, mUnreachedPaint);

        // reached
        mReachedRectF.left = mUnReachedRect.left;
        mReachedRectF.top = mUnReachedRect.top;
        mReachedRectF.right = getWidth() / 2;
        mReachedRectF.bottom = mUnReachedRect.bottom;
        canvas.drawRoundRect(mReachedRectF, mReachedHeight / 2, mReachedHeight / 2, mReachedPaint);
    }
}
