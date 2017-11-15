package com.cc.custom.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cc.custom.R;
import com.cc.custom.bubble.BubbleDrawable;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/11/15.
 */
public class BubbleProgress extends View {

    private Paint mFramePaint;
    private Paint mReachedPaint;
    private Paint mUnreachedPaint;
    private Paint mTextPaint;

    private RectF mFrameRectF = new RectF(0, 0, 0, 0);
    private RectF mBubbleRectF = new RectF(0, 0, 0, 0);

    private int mFrameLineWidth = 4;
    private int mFrameHeight = 48;
    private int mUnreachedHeight = 24;
    private int mReachedHeight = 24;

    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;

    private BubbleDrawable mBubbleDrawable;
    private CurvedAndTiled mTile;

    public BubbleProgress(Context context) {
        this(context, null);
    }

    public BubbleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(14);

        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setColor(Color.GRAY);
        mFramePaint.setStrokeWidth(mFrameLineWidth);
        mFramePaint.setStyle(Paint.Style.STROKE);

        mReachedPaint = new Paint();
        mReachedPaint.setAntiAlias(true);
        mReachedPaint.setColor(Color.BLUE);
        mReachedPaint.setStrokeCap(Paint.Cap.ROUND);
        mReachedPaint.setStrokeWidth(mReachedHeight);
        mReachedPaint.setStyle(Paint.Style.FILL);

        mUnreachedPaint = new Paint();
        mUnreachedPaint.setAntiAlias(true);
        mUnreachedPaint.setColor(Color.BLACK);
        mUnreachedPaint.setStrokeCap(Paint.Cap.ROUND);
        mUnreachedPaint.setStrokeWidth(mUnreachedHeight);
        mUnreachedPaint.setStyle(Paint.Style.FILL);
        Shader shader =
            new LinearGradient(30, 15, 15, 30, Color.parseColor("#333333"), Color.BLACK, Shader.TileMode.REPEAT);
        mUnreachedPaint.setShader(shader);

        mBubbleRectF.left = 0;
        mBubbleRectF.top = 0;
        mBubbleRectF.right = 170;
        mBubbleRectF.bottom = 100;
        mBubbleDrawable =
            new BubbleDrawable.Builder().rect(mBubbleRectF).arrowLocation(BubbleDrawable.ArrowLocation.BOTTOM)
                .bubbleType(BubbleDrawable.BubbleType.COLOR).arrowHeight(10).arrowWidth(10).arrowCenter(true).build();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slice);
        mUnreachedHeight = mReachedHeight = bitmap.getHeight();
        mTile =
            new CurvedAndTiled(bitmap, mUnreachedHeight / 2);
    }

    public void setProgress(int progress) {
        mCurrentProgress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
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
        mBubbleRectF.left = mCurrentProgress;
        mBubbleRectF.top = 0;
        mBubbleRectF.right = 170 + mCurrentProgress;
        mBubbleRectF.bottom = 100;
        // mBubbleDrawable.setBounds((int) mBubbleRectF.left, (int) mBubbleRectF.top, (int) mBubbleRectF.right,
        // (int) mBubbleRectF.bottom);
        mBubbleDrawable.draw(canvas);

        int radius = mFrameHeight / 2;

        // frame
        mFrameRectF.left = mFrameLineWidth / 2;
        mFrameRectF.top = mBubbleRectF.height() + mFrameLineWidth / 2;
        mFrameRectF.right = getWidth() - mFrameLineWidth / 2;
        mFrameRectF.bottom = mBubbleRectF.height() + mFrameHeight;
        canvas.drawRoundRect(mFrameRectF, radius, radius, mFramePaint);

        // unreached
        // canvas.drawLine(radius, mBubbleRectF.height() + mFrameHeight / 2, getWidth() - radius,
        // mBubbleRectF.height() + mFrameHeight / 2, mUnreachedPaint);
        mTile.onBoundsChange(new Rect(radius, (int) (mBubbleRectF.height() + mFrameHeight / 2 - mUnreachedHeight / 2),
            getWidth() - radius, (int) (mBubbleRectF.height() + mFrameHeight / 2 + mUnreachedHeight / 2)));
        mTile.draw(canvas);

        // reached
        canvas.drawLine(radius, mBubbleRectF.height() + mFrameHeight / 2, getWidth() / 2,
            mBubbleRectF.height() + mFrameHeight / 2, mReachedPaint);
    }
}
