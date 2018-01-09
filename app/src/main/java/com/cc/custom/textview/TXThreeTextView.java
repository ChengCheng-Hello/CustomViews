package com.cc.custom.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cc.custom.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.text.TextUtils.TruncateAt.END;

/**
 * 单行展示的3个并排文本字段，xx根据总宽度动态是否打点省略，(也适用两个label，第三个文本不设置即可)
 * 
 * 情况一：已选xx人，前后label宽度固定，中间长度可变
 * 
 * 情况二：xx评价xx，中间label宽度固定，前后label可变
 * <p>
 * Created by Cheng on 2018/1/5.
 */
public class TXThreeTextView extends View {

    // 情况一：一个label长度可变，已选xx人，前后label宽度固定，中间长度可变(也可能xx固定固定)
    private static final int AUTO_TYPE_ONE = 1;
    // 情况二：两个label长度可变，xx评价xx，中间label宽度固定，前后label可变
    private static final int AUTO_TYPE_TWO = 2;

    @IntDef({ AUTO_TYPE_ONE, AUTO_TYPE_TWO })
    @Retention(RetentionPolicy.SOURCE)
    private @interface AUTO_TYPE {
    }

    // 情况一：第一个label长度可变
    private static final int ELLIPSIS_TYPE_FIRST = 1;
    // 情况一：第二个label长度可变
    private static final int ELLIPSIS_TYPE_SECOND = 2;
    // 情况二：一、三label长度可变
    private static final int ELLIPSIS_TYPE_FIRST_AND_THIRD = 3;
    // 情况二：一、二label长度可变
    private static final int ELLIPSIS_TYPE_FIRST_AND_SECOND = 4;
    // 情况二：二、三label长度可变
    private static final int ELLIPSIS_TYPE_SECOND_AND_THIRD = 5;

    @IntDef({ ELLIPSIS_TYPE_FIRST, ELLIPSIS_TYPE_SECOND, ELLIPSIS_TYPE_FIRST_AND_THIRD, ELLIPSIS_TYPE_FIRST_AND_SECOND,
        ELLIPSIS_TYPE_SECOND_AND_THIRD })
    @Retention(RetentionPolicy.SOURCE)
    private @interface ELLIPSIS_TYPE {
    }

    // 不需要省略打点
    private static final int ELLIPSIS_NONE = -1;
    // 第一个label省略打点
    private static final int ELLIPSIS_FIRST = 1;
    // 第二个label省略打点
    private static final int ELLIPSIS_SECOND = 2;
    // 第三个label省略打点
    private static final int ELLIPSIS_THIRD = 3;
    // 一、三都省略
    private static final int ELLIPSIS_FIRST_AND_THIRD = 4;
    // 一、二都省略
    private static final int ELLIPSIS_FIRST_AND_SECOND = 5;
    // 二、三都省略
    private static final int ELLIPSIS_SECOND_AND_THIRD = 6;

    private TextPaint mFirstPaint;
    private TextPaint mSecondPaint;
    private TextPaint mThirdPaint;
    private String mFirstText;
    private String mSecondText;
    private String mThirdText;
    @ColorInt
    private int mFirstTextColor;
    @ColorInt
    private int mSecondTextColor;
    @ColorInt
    private int mThirdTextColor;
    private float mFirstTextSize;
    private float mSecondTextSize;
    private float mThirdTextSize;
    private int mAutoType = AUTO_TYPE_ONE;
    private int mEllipsisType = ELLIPSIS_TYPE_FIRST;
    private float mFirstSpace = 0;
    private float mSecondSpace = 0;

    private float mHeight;
    private float mFirstWidth;
    private float mSecondWidth;
    private float mThirdWidth;
    private Paint.FontMetrics mFirstFm;
    private Paint.FontMetrics mSecondFm;
    private Paint.FontMetrics mThirdFm;
    private int mEllipsisIndexType = ELLIPSIS_NONE;

    public TXThreeTextView(Context context) {
        this(context, null);
    }

    public TXThreeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXThreeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TXThreeTextView);
        if (a != null) {
            mFirstText = a.getString(R.styleable.TXThreeTextView_firstText);
            mSecondText = a.getString(R.styleable.TXThreeTextView_secondText);
            mThirdText = a.getString(R.styleable.TXThreeTextView_thirdText);
            mFirstTextColor = a.getColor(R.styleable.TXThreeTextView_firstTextColor, Color.BLACK);
            mSecondTextColor = a.getColor(R.styleable.TXThreeTextView_secondTextColor, Color.BLACK);
            mThirdTextColor = a.getColor(R.styleable.TXThreeTextView_thirdTextColor, Color.BLACK);
            float defaultTextSize = getResources().getDimension(R.dimen.TX_FT_BODY);
            mFirstTextSize = a.getDimension(R.styleable.TXThreeTextView_firstTextSize, defaultTextSize);
            mSecondTextSize = a.getDimension(R.styleable.TXThreeTextView_secondTextSize, defaultTextSize);
            mThirdTextSize = a.getDimension(R.styleable.TXThreeTextView_thirdTextSize, defaultTextSize);
            mAutoType = a.getInt(R.styleable.TXThreeTextView_autoType, AUTO_TYPE_ONE);
            mEllipsisType = a.getInt(R.styleable.TXThreeTextView_ellipsisType, ELLIPSIS_FIRST);
            mFirstSpace = a.getDimension(R.styleable.TXThreeTextView_firstSpace, 0);
            mSecondSpace = a.getDimension(R.styleable.TXThreeTextView_secondSpace, 0);
            a.recycle();
        }

        init();
    }

    private void init() {
        mFirstPaint = new TextPaint();
        mFirstPaint.setAntiAlias(true);
        mFirstPaint.setColor(mFirstTextColor);
        mFirstPaint.setTextAlign(Paint.Align.LEFT);
        mFirstPaint.setTextSize(mFirstTextSize);

        mSecondPaint = new TextPaint();
        mSecondPaint.setAntiAlias(true);
        mSecondPaint.setColor(mSecondTextColor);
        mSecondPaint.setTextAlign(Paint.Align.LEFT);
        mSecondPaint.setTextSize(mSecondTextSize);

        mThirdPaint = new TextPaint();
        mThirdPaint.setAntiAlias(true);
        mThirdPaint.setColor(mThirdTextColor);
        mThirdPaint.setTextAlign(Paint.Align.LEFT);
        mThirdPaint.setTextSize(mThirdTextSize);

        mFirstFm = mFirstPaint.getFontMetrics();
        mSecondFm = mSecondPaint.getFontMetrics();
        mThirdFm = mThirdPaint.getFontMetrics();
        float firstHeight = mFirstFm.bottom - mFirstFm.top;
        float secondHeight = mSecondFm.bottom - mSecondFm.top;
        float thirdHeight = mThirdFm.bottom - mThirdFm.top;
        mHeight = firstHeight;
        if (secondHeight > mHeight) {
            mHeight = secondHeight;
        }
        if (thirdHeight > mHeight) {
            mHeight = thirdHeight;
        }

        if (mFirstText != null) {
            mFirstWidth = mFirstPaint.measureText(mFirstText);
        } else {
            mFirstWidth = 0;
        }
        if (mSecondText != null) {
            mSecondWidth = mSecondPaint.measureText(mSecondText);
        } else {
            mSecondWidth = 0;
        }
        if (mThirdText != null) {
            mThirdWidth = mThirdPaint.measureText(mThirdText);
        } else {
            mThirdWidth = 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        if (width <= 0) {
            return;
        }
        setMeasuredDimension(width, (int) mHeight);

        float defaultWidth = mFirstWidth + mSecondWidth + mThirdWidth + mFirstSpace + mSecondSpace;

        if (width < defaultWidth) {
            if (mAutoType == AUTO_TYPE_ONE) {
                // 情况一
                if (mEllipsisType == ELLIPSIS_TYPE_FIRST) {
                    // 省略第一个
                    mEllipsisIndexType = ELLIPSIS_FIRST;
                    mFirstWidth = width - mSecondWidth - mThirdWidth - mFirstSpace - mSecondSpace;
                } else if (mEllipsisType == ELLIPSIS_TYPE_SECOND) {
                    // 省略第二个
                    mEllipsisIndexType = ELLIPSIS_SECOND;
                    mSecondWidth = width - mFirstWidth - mThirdWidth - mFirstSpace - mSecondSpace;
                }
            } else if (mAutoType == AUTO_TYPE_TWO) {
                // 情况二
                // 如果两个可变都超长的最大宽度
                float halfMaxWidth;
                if (mEllipsisType == ELLIPSIS_TYPE_FIRST_AND_THIRD) {
                    // 一、三label长度可变
                    halfMaxWidth = (width - mSecondWidth - mFirstSpace - mSecondSpace) / 2;
                    if (mFirstWidth > halfMaxWidth && mThirdWidth > halfMaxWidth) {
                        // 一、三都省略
                        mEllipsisIndexType = ELLIPSIS_FIRST_AND_THIRD;
                        mFirstWidth = mThirdWidth = (width - mSecondWidth - mFirstSpace - mSecondSpace) / 2;
                    } else if (mFirstWidth > halfMaxWidth) {
                        // 一省略
                        mEllipsisIndexType = ELLIPSIS_FIRST;
                        mFirstWidth = width - mSecondWidth - mThirdWidth - mFirstSpace - mSecondSpace;
                    } else if (mThirdWidth > halfMaxWidth) {
                        // 三省略
                        mEllipsisIndexType = ELLIPSIS_THIRD;
                        mThirdWidth = width - mFirstWidth - mSecondWidth - mFirstSpace - mSecondSpace;
                    }
                } else if (mEllipsisType == ELLIPSIS_TYPE_FIRST_AND_SECOND) {
                    // 一、二label长度可变
                    halfMaxWidth = (width - mThirdWidth - mFirstSpace - mSecondSpace) / 2;
                    if (mFirstWidth > halfMaxWidth && mSecondWidth > halfMaxWidth) {
                        // 一、二都省略
                        mEllipsisIndexType = ELLIPSIS_FIRST_AND_SECOND;
                        mFirstWidth = mSecondWidth = (width - mThirdWidth - mFirstSpace - mSecondSpace) / 2;
                    } else if (mFirstWidth > halfMaxWidth) {
                        // 一省略
                        mEllipsisIndexType = ELLIPSIS_FIRST;
                        mFirstWidth = width - mSecondWidth - mThirdWidth - mFirstSpace - mSecondSpace;
                    } else if (mSecondWidth > halfMaxWidth) {
                        // 二省略
                        mEllipsisIndexType = ELLIPSIS_SECOND;
                        mSecondWidth = width - mFirstWidth - mThirdWidth - mFirstSpace - mSecondSpace;
                    }
                } else if (mEllipsisType == ELLIPSIS_TYPE_SECOND_AND_THIRD) {
                    // 二、三label长度可变
                    halfMaxWidth = (width - mFirstWidth - mFirstSpace - mSecondSpace) / 2;
                    if (mSecondWidth > halfMaxWidth && mThirdWidth > halfMaxWidth) {
                        // 二、三都省略
                        mEllipsisIndexType = ELLIPSIS_SECOND_AND_THIRD;
                        mSecondWidth = mThirdWidth = (width - mFirstWidth) / 2 - mFirstSpace;
                    } else if (mSecondWidth > halfMaxWidth) {
                        // 二省略
                        mEllipsisIndexType = ELLIPSIS_SECOND;
                        mSecondWidth = width - mFirstWidth - mThirdWidth - mFirstSpace - mSecondSpace;
                    } else if (mSecondWidth > halfMaxWidth) {
                        // 三省略
                        mEllipsisIndexType = ELLIPSIS_THIRD;
                        mThirdWidth = width - mFirstWidth - mSecondWidth - mFirstSpace - mSecondSpace;
                    }
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 高度居中
        float firstBaseLine = (mHeight - mFirstFm.top - mFirstFm.bottom) / 2;
        float secondBaseLine = (mHeight - mSecondFm.top - mSecondFm.bottom) / 2;
        float thirdBaseLine = (mHeight - mThirdFm.top - mThirdFm.bottom) / 2;

        if (mAutoType == AUTO_TYPE_ONE) {
            // 情况一
            if (mEllipsisIndexType == ELLIPSIS_NONE) {
                // 不需要省略
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
            } else if (mEllipsisIndexType == ELLIPSIS_FIRST) {
                // 省略第一个
                CharSequence ellipsizeText = TextUtils.ellipsize(mFirstText, mFirstPaint, mFirstWidth, END);
                mFirstText = ellipsizeText.toString();
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                mFirstWidth = mFirstPaint.measureText(mFirstText);
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
            } else if (mEllipsisIndexType == ELLIPSIS_SECOND) {
                // 省略第二个
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                CharSequence ellipsizeText = TextUtils.ellipsize(mSecondText, mSecondPaint, mSecondWidth, END);
                mSecondText = ellipsizeText.toString();
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                mSecondWidth = mSecondPaint.measureText(mSecondText);
            }
            canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                mThirdPaint);
        } else if (mAutoType == AUTO_TYPE_TWO) {
            // 情况二
            if (mEllipsisIndexType == ELLIPSIS_NONE) {
                // 不需要省略
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                    mThirdPaint);
            } else if (mEllipsisIndexType == ELLIPSIS_FIRST_AND_THIRD) {
                // 一、三都省略
                CharSequence firstEllipsizeText = TextUtils.ellipsize(mFirstText, mFirstPaint, mFirstWidth, END);
                mFirstText = firstEllipsizeText.toString();
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                mFirstWidth = mFirstPaint.measureText(mFirstText);
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                CharSequence thirdEllipsizeText = TextUtils.ellipsize(mThirdText, mThirdPaint, mThirdWidth, END);
                mThirdText = thirdEllipsizeText.toString();
                canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                    mThirdPaint);
                mThirdWidth = mThirdPaint.measureText(mThirdText);
            } else if (mEllipsisIndexType == ELLIPSIS_FIRST_AND_SECOND) {
                // 一、二都省略
                CharSequence firstEllipsizeText = TextUtils.ellipsize(mFirstText, mFirstPaint, mFirstWidth, END);
                mFirstText = firstEllipsizeText.toString();
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                mFirstWidth = mFirstPaint.measureText(mFirstText);
                CharSequence secondEllipsizeText = TextUtils.ellipsize(mSecondText, mSecondPaint, mSecondWidth, END);
                mSecondText = secondEllipsizeText.toString();
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                mSecondWidth = mSecondPaint.measureText(mSecondText);
                canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                    mThirdPaint);
            } else if (mEllipsisIndexType == ELLIPSIS_SECOND_AND_THIRD) {
                // 二、三都省略
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                CharSequence secondEllipsizeText = TextUtils.ellipsize(mSecondText, mSecondPaint, mSecondWidth, END);
                mSecondText = secondEllipsizeText.toString();
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                mSecondWidth = mSecondPaint.measureText(mSecondText);
                CharSequence thirdEllipsizeText = TextUtils.ellipsize(mThirdText, mThirdPaint, mThirdWidth, END);
                mThirdText = thirdEllipsizeText.toString();
                canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                    mThirdPaint);
                mThirdWidth = mThirdPaint.measureText(mThirdText);
            } else if (mEllipsisIndexType == ELLIPSIS_FIRST) {
                // 一省略
                CharSequence ellipsizeText = TextUtils.ellipsize(mFirstText, mFirstPaint, mFirstWidth, END);
                mFirstText = ellipsizeText.toString();
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                mFirstWidth = mFirstPaint.measureText(mFirstText);
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                    mThirdPaint);
            } else if (mEllipsisIndexType == ELLIPSIS_SECOND) {
                // 二省略
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                CharSequence ellipsizeText = TextUtils.ellipsize(mSecondText, mSecondPaint, mSecondWidth, END);
                mSecondText = ellipsizeText.toString();
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                mSecondWidth = mSecondPaint.measureText(mSecondText);
                canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                    mThirdPaint);
            } else if (mEllipsisIndexType == ELLIPSIS_THIRD) {
                // 三省略
                canvas.drawText(mFirstText, 0, firstBaseLine, mFirstPaint);
                canvas.drawText(mSecondText, mFirstWidth + mFirstSpace, secondBaseLine, mSecondPaint);
                CharSequence ellipsizeText = TextUtils.ellipsize(mThirdText, mThirdPaint, mThirdWidth, END);
                mThirdText = ellipsizeText.toString();
                canvas.drawText(mThirdText, mFirstWidth + mSecondWidth + mFirstSpace + mSecondSpace, thirdBaseLine,
                    mThirdPaint);
                mThirdWidth = mThirdPaint.measureText(mThirdText);
            }
        }
    }

    /**
     * 设置第一个label文本
     *
     * @param firstText
     */
    public void setFirstText(String firstText) {
        mFirstText = firstText;
        if (mFirstText != null) {
            mFirstWidth = mFirstPaint.measureText(mFirstText);
        } else {
            mFirstWidth = 0;
        }
        requestLayout();
    }

    /**
     * 设置第二个label文本
     *
     * @param secondText
     */
    public void setSecondText(String secondText) {
        mSecondText = secondText;
        if (mSecondText != null) {
            mSecondWidth = mSecondPaint.measureText(mSecondText);
        } else {
            mSecondWidth = 0;
        }
        requestLayout();
    }

    /**
     * 设置第三个label文本
     *
     * @param thirdText
     */
    public void setThirdText(String thirdText) {
        mThirdText = thirdText;
        if (mThirdText != null) {
            mThirdWidth = mThirdPaint.measureText(mThirdText);
        } else {
            mThirdWidth = 0;
        }
        requestLayout();
    }
}