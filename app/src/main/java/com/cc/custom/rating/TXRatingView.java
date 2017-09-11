package com.cc.custom.rating;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
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

    private Bitmap mEmptyBitmap;
    private Bitmap mFilledBitmap;

    private Paint mPaint;

    private int[] mStartArray;

    private int mMaxRating;
    private int mRating;
    private int mRatingSize;
    private int mItemSpace;
    private boolean mEnabled;

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
        if (rating < 0 || rating > mMaxRating) {
            return;
        }

        mRating = rating;

        invalidate();
    }

    /**
     * 获取评分
     */
    public int getRating() {
        return mRating;
    }

    private void init() {
        mStartArray = new int[mMaxRating];

        mEmptyBitmap = BitmapFactory.decodeResource(getResources(), mEmptyResourceId);
        mFilledBitmap = BitmapFactory.decodeResource(getResources(), mFilledResourceId);

        int width = mEmptyBitmap.getWidth();
        if (width != mRatingSize) {
            mEmptyBitmap = Bitmap.createScaledBitmap(mEmptyBitmap, mRatingSize, mRatingSize, false);
            mFilledBitmap = Bitmap.createScaledBitmap(mFilledBitmap, mRatingSize, mRatingSize, false);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

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

        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = 0, len = mMaxRating; i < len; i++) {
            if (i > 0) {
                left += mRatingSize + mItemSpace;
            }
            mStartArray[i] = left + mRatingSize;
            if (i < mRating) {
                // filled
                canvas.drawBitmap(mFilledBitmap, left, top, mPaint);
            } else {
                // empty
                canvas.drawBitmap(mEmptyBitmap, left, top, mPaint);
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
        int rating = -1;
        for (int i = 0; i < mMaxRating; i++) {
            int startX = mStartArray[i];
            if (e.getX() <= startX) {
                rating = i;
                break;
            }
        }

        rating++;

        if (rating < 0 && rating > mMaxRating) {
            return;
        }

//        if (rating == mRating) {
//            mRating = mRating - 1;
//            if (mRating < 0) {
//                mRating = 0;
//            }
//        } else {
            mRating = rating;
//        }

        invalidate();
    }

    /**
     * 处理移动事件
     *
     * @param e event
     */
    private void handleMoveEvent(MotionEvent e) {
        int rating = -1;
        float x = e.getX();
        for (int i = 0; i < mMaxRating; i++) {
            int startX = mStartArray[i] + mRatingSize / 2;
            if (x <= 0) {
                break;
            } else if (x >= mStartArray[mMaxRating - 1]) {
                rating = mMaxRating;
                break;
            } else if (e.getX() <= startX) {
                rating = i;
                break;
            }
        }

        if (rating < 0) {
            mRating = 0;
        } else if (rating >= mMaxRating) {
            mRating = mMaxRating;
        } else {
            rating++;
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

        private int rating;

        private SavedState(Parcel source) {
            super(source);
            rating = source.readInt();
        }

        private SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(rating);
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
