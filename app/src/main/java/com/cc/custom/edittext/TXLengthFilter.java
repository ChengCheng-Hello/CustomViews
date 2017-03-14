package com.cc.custom.edittext;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Cheng on 17/2/27.
 */

public class TXLengthFilter implements InputFilter {

    private final int mMax;
    private TXEditTextLimitListener mLengthListener;

    public TXLengthFilter(int max) {
        this.mMax = max;
    }

    public TXLengthFilter(int max, TXEditTextLimitListener listener) {
        mMax = max;
        mLengthListener = listener;
    }

    /**
     * 参考LengthFilter，只是添加了TXEditTextLimitListener回调
     */
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            if (mLengthListener != null) {
                mLengthListener.onReachLimit();
            }
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }

            if (mLengthListener != null) {
                mLengthListener.onReachLimit();
            }
            return source.subSequence(start, keep);
        }
    }

    /**
     * @return the maximum length enforced by this input filter
     */
    public int getMax() {
        return mMax;
    }
}
