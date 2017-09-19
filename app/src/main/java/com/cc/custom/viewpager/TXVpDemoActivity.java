package com.cc.custom.viewpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.custom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager设置Transformer显示有问题。
 * 
 * 对比发现如ViewPager设置GONE，设置数据时才设置成VISIBLE，这样就是OK的，否则有问题。
 * 
 * 1.transformer显示大小不对：因为child.getWidth == 0，怀疑测量问题。
 * 
 * 2. VISIBLE和GONE的区别也是绘制测量时机。
 * 
 * 情况一： 默认XML里ViewPager是VISIBLE的。
 * 
 * step1，Activity.onCreate()初始化
 * 
 * init —> onMeasure ( child == 0 ) —> onLayout —> isFirstLayout ? —> scrollToItem —> onPageScrolled —> child == 0 not
 * call transformPage
 * 
 * step2，ViewPager.setData()设置数据
 * 
 * adapter.notifyDataChanged —> vp.onChanged —> dataSetChanged() —> setCurrentItemInternal() —> isFirstLayout ? —>
 * scrollToItem —> onPageScrolled —>transformPage —> child.getWidth == 0
 * 
 * 情况二： 默认XML里ViewPager是GONE的。
 * 
 * step1，Activity.onCreate()初始化 do nothing
 * 
 * step2，ViewPager.setVisible(VISIBLE) + ViewPager.setData()
 * 
 * init —> onMeasure(child > 0) —> onLayout —> isFirstLayout ? —> scrollToItem —> onPageScrolled —> transformPage >
 * child.getWidth > 0
 * 
 * 所以，最保险的做法是setData之后再setVisible
 * <p>
 * Created by Cheng on 2017/9/19.
 */
public class TXVpDemoActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private MyAdapter mAdapter;

    // private ViewPager mViewPager2;
    // private MyAdapter2 mAdapter2;

    private List<String> mList;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXVpDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp_demo);

        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        // mViewPager2 = (ViewPager) findViewById(R.id.vp_2);

        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);

        // mAdapter2 = new MyAdapter2();
        // mViewPager2.setAdapter(mAdapter2);

        TXScaleSwitchTransformer transformer = new TXScaleSwitchTransformer();
        mViewPager.setPageTransformer(false, transformer);
        // mViewPager2.setPageTransformer(false, transformer);

        mList = new ArrayList<>();
        mList.add("1: hh1");
        mList.add("2: hh2");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_1) {
            mAdapter.setData(mList);
            // mAdapter2.setData(mList);
        } else if (id == R.id.btn) {
            mViewPager.setVisibility(View.VISIBLE);
        }
    }

    private static class MyAdapter extends PagerAdapter {

        private List<String> list;

        public void setData(List<String> list) {
            this.list = list;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_vp_demo, container, false);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvContent.setText(list.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private static class MyAdapter2 extends PagerAdapter {

        private List<String> list;

        public void setData(List<String> list) {
            this.list = list;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_vp_demo2, container, false);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvContent.setText(list.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
