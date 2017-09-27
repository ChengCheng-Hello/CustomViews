/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.cc.custom.uikit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cc.custom.R;

import java.util.Locale;

public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface TabTitleProvider {

        // 使用图标 title
        public int getPageIconResId(int position);

        // 使用自定义View title
        public View getPageTabView(int position);

        // 使用 charsequence title
        public CharSequence getPageTabText(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[] { android.R.attr.textSize, android.R.attr.textColor };
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int firstPosition = 0;
    private int lastSelectedPosition = -1;
    private int selectedPosition = 0;
    private float firstPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF007BFF;
    private int underlineColor = 0xFFDDDDDD;
    private int dividerColor = 0xFFDDDDDD;

    private boolean shouldExpand = false;
    private boolean textAllCaps = false;

    private float scrollOffset = 26;
    private float indicatorHeight = 1;
    private float indicatorPadding = 10;
    private float underlineHeight = 0.5f;
    private float dividerPadding = 0;
    private float tabPadding = 0;
    private float dividerWidth = 0.5f;

    private float tabTextSize = 14;
    private int tabTextColor = 0xFF666666;
    private int tabSelectedTextColor = 0xFF007BFF;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;

    private int tabBackgroundResId = 0;
    private int tabSelectedBackgroundResId = 0;

    private Locale locale;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        indicatorPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorPadding, dm);
        tabPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get custom attrs

        TypedArray a= context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        tabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsTabTextColor, tabTextColor);
        tabSelectedTextColor =
            a.getColor(R.styleable.PagerSlidingTabStrip_pstsTabSelectedTextColor, tabSelectedTextColor);
        tabTextSize = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabTextSize, (int) tabTextSize);
        indicatorHeight =
            a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, (int) indicatorHeight);
        underlineHeight =
            a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, (int) underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, (int) dividerPadding);
        indicatorPadding = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_pstIndicatorPadding, (int) indicatorPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, (int) tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        tabSelectedBackgroundResId =
            a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabSelectedBackgrground, tabSelectedBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, (int) scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.addOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof TabTitleProvider) {
                // 实现 tabtitleprovider 接口
                if (((TabTitleProvider) pager.getAdapter()).getPageTabText(i) != null) {
                    // 添加 charsequence 标题
                    addTextTab(i, ((TabTitleProvider) pager.getAdapter()).getPageTabText(i));
                    continue;
                } else if (((TabTitleProvider) pager.getAdapter()).getPageTabView(i) != null) {
                    // 添加自定义 View 标题
                    addTab(i, ((TabTitleProvider) pager.getAdapter()).getPageTabView(i));
                    continue;
                } else if (((TabTitleProvider) pager.getAdapter()).getPageIconResId(i) > 0) {
                    // 添加 icon 标题
                    addIconTab(i, ((TabTitleProvider) pager.getAdapter()).getPageIconResId(i));
                    continue;
                }
            }
            // 未实现 tabtitleprovider 接口,使用 adapter 默认实现
            addTextTab(i, pager.getAdapter().getPageTitle(i));

        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                firstPosition = pager.getCurrentItem();
                scrollToChild(firstPosition, 0);
                View tab = tabsContainer.getChildAt(firstPosition);
                if (tab instanceof TextView) {
                    ((TextView) tab).setTextColor(tabSelectedTextColor);
                }
                if (tabSelectedBackgroundResId > 0) {
                    tab.setBackgroundResource(tabSelectedBackgroundResId);
                }
            }
        });

    }

    private void addTextTab(final int position, CharSequence title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();

        addTab(position, tab);
    }

    private void addIconTab(final int position, int resId) {

        ImageView tab = new ImageView(getContext());
        tab.setImageResource(resId);
        tab.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addTab(position, tab);

    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tab.setPadding((int) tabPadding, 0, (int) tabPadding, 0);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            if (tabSelectedBackgroundResId > 0) {
                v.setBackgroundResource(tabBackgroundResId);
            }

            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }

    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        View currentTab = tabsContainer.getChildAt(firstPosition);

        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (firstPositionOffset > 0f && firstPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(firstPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (firstPositionOffset * nextTabLeft + (1f - firstPositionOffset) * lineLeft);
            lineRight = (firstPositionOffset * nextTabRight + (1f - firstPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft + indicatorPadding, height - indicatorHeight, lineRight - indicatorPadding, height, rectPaint);

        // draw underline

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw divider

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }

    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            firstPosition = position;
            firstPositionOffset = positionOffset;

            // scroll to intented tab
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            // redraw indicator , divider and line
            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            lastSelectedPosition = selectedPosition;
            selectedPosition = position;
            // 设置选中 tab 的字体和背景色
            View currentTab = tabsContainer.getChildAt(selectedPosition);
            if (currentTab instanceof TextView) {
                ((TextView) currentTab).setTextColor(tabSelectedTextColor);
            }
            if (tabSelectedBackgroundResId > 0) {
                currentTab.setBackgroundResource(tabSelectedBackgroundResId);
            }
            View lastTab = tabsContainer.getChildAt(lastSelectedPosition);
            if (lastTab instanceof TextView) {
                ((TextView) lastTab).setTextColor(tabTextColor);
            }
            if (tabBackgroundResId > 0) {
                lastTab.setBackgroundResource(tabBackgroundResId);
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setTabTextColor(int tabTextColor) {
        this.tabTextColor = tabTextColor;
        invalidate();
    }

    public int getTabTextColor() {
        return tabTextColor;
    }

    public void setTabSelectedTextColor(int tabSelectedTextColor) {
        this.tabSelectedTextColor = tabSelectedTextColor;
        invalidate();
    }

    public int getTabSelectedTextColor() {
        return tabSelectedTextColor;
    }

    public void setTabBackgroundResId(int tabBackgroundResId) {
        this.tabBackgroundResId = tabBackgroundResId;
        invalidate();
    }

    public int getTabBackgroundResId() {
        return tabBackgroundResId;
    }

    public void setTabSelectedBackgroundResId(int tabSelectedBackgroundResId) {
        this.tabSelectedBackgroundResId = tabSelectedBackgroundResId;
        invalidate();
    }

    public int getTabSelectedBackgroundResId() {
        return tabSelectedBackgroundResId;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setTabTextSize(int tabTextSize) {
        this.tabTextSize = tabTextSize;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public float getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorPadding(int indicatorPadding) {
        this.indicatorPadding = indicatorPadding;
        invalidate();
    }

    public float getIndicatorPadding() {
        return indicatorPadding;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public float getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public float getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public float getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public float getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public float getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        firstPosition = savedState.firstPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.firstPosition = firstPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int firstPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            firstPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(firstPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
