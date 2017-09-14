package com.cc.custom.rating;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.cc.custom.R;

/**
 * 评分View
 * <p>
 * Created by Cheng on 2017/9/7.
 */
public class TXRatingView extends View {

    private static final String TAG = "TXRatingView";

    private static final int DEFAULT_MAX_RATING = 5;
    private static final int DEFAULT_RATING = 0;

    private int mEmptyResourceId;
    private int mFilledResourceId;

    private Drawable mEmptyDrawable;
    private Drawable mFilledDrawable;

    private Paint mHintPaint;
    private Paint mRatingValuePaint;
    private Paint mRatingPaint;

    private int[] mEndArray;

    private int mMaxRating;
    private float mRating;
    private int mRatingSize;
    private int mItemSpace;
    private boolean mEnabled;

    private int mHeaderHeight;
    private int mTextSpace;
    private String mHintText;
    private String mRatingText;

    private GestureDetectorCompat mGestureDetector;

    public TXRatingView(Context context) {
        this(context, null);
    }

    public TXRatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXRatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TXRatingView);
        if (typedArray != null) {
            mMaxRating = typedArray.getInt(R.styleable.TXRatingView_maxRating, DEFAULT_MAX_RATING);
            mRating = typedArray.getInt(R.styleable.TXRatingView_rating, DEFAULT_RATING);
            mEmptyResourceId =
                typedArray.getResourceId(R.styleable.TXRatingView_emptyDrawable, R.drawable.rating_empty);
            mFilledResourceId =
                typedArray.getResourceId(R.styleable.TXRatingView_filledDrawable, R.drawable.rating_filled);
            mRatingSize = typedArray.getDimensionPixelOffset(R.styleable.TXRatingView_ratingSize,
                getResources().getDimensionPixelOffset(R.dimen.tx_rating_item_default_size));
            mItemSpace = typedArray.getDimensionPixelOffset(R.styleable.TXRatingView_itemSpace,
                getResources().getDimensionPixelOffset(R.dimen.tx_rating_item_default_space));
            mEnabled = typedArray.getBoolean(R.styleable.TXRatingView_enabled, false);
            typedArray.recycle();
        }

        init();
    }

    /**
     * 设置评分
     *
     * @param rating 评分
     */
    public void setRating(int rating) {
        float realRating = rating / 100f;

        if (realRating < 0 || realRating > mMaxRating) {
            return;
        }

        mRating = realRating;

        invalidate();
    }

    /**
     * 获取评分
     */
    public int getRating() {
        return (int) (mRating * 100);
    }

    private void init() {
        mEndArray = new int[mMaxRating * 2];

        mEmptyDrawable = getResources().getDrawable(mEmptyResourceId);
        mFilledDrawable = getResources().getDrawable(mFilledResourceId);

        mEmptyDrawable =
            new ClipDrawable(mEmptyDrawable.getConstantState().newDrawable(), Gravity.END, ClipDrawable.HORIZONTAL);
        mFilledDrawable =
            new ClipDrawable(mFilledDrawable.getConstantState().newDrawable(), Gravity.START, ClipDrawable.HORIZONTAL);

        mHintPaint = new Paint();
        mHintPaint.setAntiAlias(true);
        mHintPaint.setColor(Color.BLUE);
        mHintPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.tx_rating_hint_text_size));

        mRatingValuePaint = new Paint();
        mRatingValuePaint.setAntiAlias(true);
        mRatingValuePaint.setColor(Color.BLUE);
        mRatingValuePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.tx_rating_value_text_size));

        mRatingPaint = new Paint();
        mRatingPaint.setAntiAlias(true);
        mRatingPaint.setColor(Color.BLUE);
        mRatingPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.tx_rating_text_size));

        mHintText = getContext().getString(R.string.tx_rating_hint_text);
        mRatingText = getContext().getString(R.string.tx_rating);
        mTextSpace = getResources().getDimensionPixelOffset(R.dimen.tx_rating_text_space);

        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.tx_rating_header_height);

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                handleMoveEvent(e2);
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                handleClickEvent(e);
                return super.onSingleTapUp(e);
            }
        };
        mGestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = mRatingSize * mMaxRating + (mMaxRating - 1) * mItemSpace;
        int height = mRatingSize;
        if (mEnabled) {
            height += mHeaderHeight;
        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = width + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = height + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float y = getHeight() - mRatingSize - mTextSpace;
        if (mRating < 0.5f) {
            // hint text
            float hintWidth = mHintPaint.measureText(mHintText);
            canvas.drawText(mHintText, (getWidth() - hintWidth) / 2, y, mHintPaint);
        } else {
            // score
            float valueWidth = mRatingValuePaint.measureText(String.valueOf(mRating));
            float x = (getWidth() - valueWidth) / 2;
            canvas.drawText(String.valueOf(mRating), x, y, mRatingValuePaint);
            canvas.drawText(mRatingText, x + valueWidth, y, mRatingPaint);
        }

        int left = getPaddingLeft();
        int top = getPaddingTop();

        if (mEnabled) {
            top += mHeaderHeight;
        }

        double minInt = Math.floor(mRating);
        boolean hasHalf = mRating % 1 > 0f;

        for (int i = 0, len = mMaxRating; i < len; i++) {
            if (i > 0) {
                left += mRatingSize + mItemSpace;
            }
            mEndArray[i] = left + mRatingSize;

            if (i < minInt) {
                // filled
                mFilledDrawable.setBounds(left, top, left + mRatingSize, top + mRatingSize);
                mFilledDrawable.setLevel(10000);
                mFilledDrawable.draw(canvas);
            } else if (i == minInt && hasHalf) {
                // may half
                mEmptyDrawable.setBounds(left, top, left + mRatingSize, top + mRatingSize);
                mEmptyDrawable.draw(canvas);
                mEmptyDrawable.setLevel(10000 / 2);
                mFilledDrawable.setBounds(left, top, left + mRatingSize, top + mRatingSize);
                mFilledDrawable.setLevel(10000 / 2);
                mFilledDrawable.draw(canvas);
            } else {
                // empty
                mEmptyDrawable.setBounds(left, top, left + mRatingSize, top + mRatingSize);
                mEmptyDrawable.setLevel(10000);
                mEmptyDrawable.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mEnabled && mGestureDetector.onTouchEvent(event);
    }

    /**
     * 处理点击事件
     *
     * @param e event
     */
    private void handleClickEvent(MotionEvent e) {
        float rating = -1;
        float x = e.getX();
        for (int i = 0; i < mMaxRating; i++) {
            int startX = mEndArray[i];
            if (x < startX - mRatingSize / 2) {
                rating = i + 0.5f;
                break;
            } else if (x <= startX) {
                rating = i + 1;
                break;
            }
        }

        if (rating < 0 && rating > mMaxRating) {
            return;
        }

        mRating = rating;

        invalidate();
    }

    /**
     * 处理移动事件
     *
     * @param e event
     */
    private void handleMoveEvent(MotionEvent e) {
        float rating = -1;
        float x = e.getX();
        for (int i = 0; i < mMaxRating; i++) {
            int startX = mEndArray[i] + mRatingSize / 2;
            if (x <= mRatingSize / 2) {
                rating = 0.5f;
                break;
            } else if (x >= mEndArray[mMaxRating - 1]) {
                rating = mMaxRating;
                break;
            } else if (x <= startX - mRatingSize * 3 / 4) {
                rating = i + 0.5f;
                break;
            } else if (x <= startX - mRatingSize / 4) {
                rating = i + 1;
                break;
            }
        }

        if (rating < 0.5f) {
            mRating = 0.5f;
        } else if (rating >= mMaxRating) {
            mRating = mMaxRating;
        } else {
            if (rating == mRating) {
                return;
            }
            mRating = rating;
        }

        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState ss = new SavedState(parcelable);
        ss.rating = mRating;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(state);
        mRating = ss.rating;
        invalidate();
    }

    private static class SavedState extends BaseSavedState {

        private float rating;

        private SavedState(Parcel source) {
            super(source);
            rating = source.readFloat();
        }

        private SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(rating);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
