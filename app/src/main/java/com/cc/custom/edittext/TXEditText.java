package com.cc.custom.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

import com.cc.custom.R;

/**
 * 实现输入长度限制，达到最大长度会toast提示
 * <p>
 * 通过属性设置长度限制，limitLength > 0 会生效
 * 提示文案默认：最多输入%1$d个字。也可使用属性自定义。
 * <p>
 * Created by Cheng on 17/2/27.
 */

public class TXEditText extends EditText implements TXEditTextLimitListener {

    private static final String TAG = "TXEditText";

    /**
     * 限制长度
     */
    private int mLimitLength;

    /**
     * 提示的文本内容
     */
    private String mLimitText;

    public TXEditText(Context context) {
        this(context, null);
    }

    public TXEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public TXEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TXEditText);
        if (ta != null) {
            mLimitLength = ta.getInt(R.styleable.TXEditText_limitLength, 0);
            mLimitText = ta.getString(R.styleable.TXEditText_limitText);
            ta.recycle();
        }

        init();
    }

    private void init() {
        if (mLimitLength > 0) {
            TXLengthFilter lengthFilter = new TXLengthFilter(mLimitLength, this);
            setFilters(new InputFilter[]{lengthFilter});
        }
    }

    @Override
    public void onReachLimit() {
        if (TextUtils.isEmpty(mLimitText)) {
            mLimitText = getContext().getString(R.string.limit_default_text);
            mLimitText = String.format(mLimitText, mLimitLength);
        }

        Toast.makeText(getContext(), mLimitText, Toast.LENGTH_SHORT).show();
    }
}
