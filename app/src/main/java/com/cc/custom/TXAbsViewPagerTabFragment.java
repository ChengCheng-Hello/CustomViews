package com.cc.custom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.custom.uikit.PagerSlidingTabStrip;
import com.cc.custom.uikit.UnScrollViewPager;

public abstract class TXAbsViewPagerTabFragment extends TXBaseFragment implements ViewPager.OnPageChangeListener {

    private static final String TAG = TXAbsViewPagerTabFragment.class.getSimpleName();

    protected UnScrollViewPager mViewPager = null;
    protected PagerSlidingTabStrip mTab = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getContentLayoutId() <= 0 ? R.layout.tx_fragment_viewpager_with_tab : getContentLayoutId();
        View view = inflater.inflate(layoutId, container, false);

        mTab = (PagerSlidingTabStrip) view.findViewById(R.id.tx_viewpager_indicator);
        if (getIndicatorWith() > 0) {
            view.findViewById(R.id.tx_viewpager_indicator).getLayoutParams().width = getIndicatorWith();
        }
        mViewPager = (UnScrollViewPager) view.findViewById(R.id.tx_viewpager_vp);
        mViewPager.setCanScroll(canScroll());
        mViewPager.setAdapter(new TXAbsViewPagerTabFragment.SampleFragmentPagerAdapter(getAdapterFragmentManager()));
        mTab.setViewPager(mViewPager);
        mTab.setOnPageChangeListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 如果是indicator类型的title可以自己设定宽度
     *
     * @return 宽度，默认是0，表示match_parent
     */
    protected int getIndicatorWith() {
        return 0;
    }

    /**
     * 获取页面资源id,返回<=0 则使用默认的
     */
    protected int getContentLayoutId() {
        return 0;
    }

    protected FragmentManager getAdapterFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    /**
     * 能否左右滑动切换tab，默认是可以，子类可以重写
     */
    protected boolean canScroll() {
        return false;
    }

    /**
     * @return fragment 页数
     */
    protected abstract int getCount();

    /**
     * @param position 第几页
     * @return 当前页的 fragment 实例
     */
    protected abstract Fragment getFragment(int position);

    /**
     * @return 当前页的标题,接受富文本
     */
    protected abstract CharSequence getFragmentTitle(int position);

    /**
     * 自定义的 title 图片
     * 
     * @param position
     * @return
     */
    protected int getFragmentTabIconRes(int position) {
        return 0;
    }

    /**
     * 自定义的title view
     */
    protected View getFragmentTabView(int position) {
        return null;
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageSelected(int arg0) {
        Log.d(TAG, "onPageSelected i:" + arg0);
    }

    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter
        implements PagerSlidingTabStrip.TabTitleProvider {

        public SampleFragmentPagerAdapter() {
            super(getActivity().getSupportFragmentManager());
        }

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TXAbsViewPagerTabFragment.this.getCount();
        }

        @Override
        public Fragment getItem(int position) {
            return TXAbsViewPagerTabFragment.this.getFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TXAbsViewPagerTabFragment.this.getFragmentTitle(position);
        }

        @Override
        public int getPageIconResId(int position) {
            int res = TXAbsViewPagerTabFragment.this.getFragmentTabIconRes(position);
            if (res > 0) {
                return res;
            }
            return 0;
        }

        @Override
        public View getPageTabView(int position) {
            View view = TXAbsViewPagerTabFragment.this.getFragmentTabView(position);
            if (view != null)
                return view;
            return null;
        }

        @Override
        public CharSequence getPageTabText(int position) {
            CharSequence charSequence = TXAbsViewPagerTabFragment.this.getFragmentTitle(position);
            if (charSequence != null)
                return charSequence;
            return null;
        }
    }

}
