package com.cc.custom.calender.demo.cell;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.custom.R;
import com.cc.custom.calender.demo.TXCalenderViewPoolManager;
import com.cc.custom.calender.demo.TXDate;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXMonthModel;
import com.cc.custom.calender.demo.model.TXYearModel;
import com.tx.listview.base.cell.TXBaseListCell;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalendarMonthCell implements TXBaseListCell<TXYearModel> {

    private TextView mTvTitle;
    private RecyclerView mRv;
    private MyAdapter mAdapter;

    public TXCalendarMonthCell(TXOnSelectDateListener selectDateRangeListener) {
        this.mAdapter = new MyAdapter(selectDateRangeListener);
    }

    @Override
    public void setData(TXYearModel model) {
        if (model == null) {
            return;
        }

        int year = model.year.date.getYear();

        mTvTitle.setText(String.format("%1$d年", year));

        mAdapter.setListData(model.monthList);
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_calendar_month;
    }

    @Override
    public void initCellViews(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mRv.setRecycledViewPool(TXCalenderViewPoolManager.getInstance().getRecyclerViewPool());
        mRv.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
        mRv.setAdapter(mAdapter);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<TXMonthModel> listData;
        private TXOnSelectDateListener selectDateRangeListener;

        public MyAdapter(TXOnSelectDateListener selectDateRangeListener) {
            this.selectDateRangeListener = selectDateRangeListener;
        }

        public void setListData(List<TXMonthModel> listData) {
            this.listData = listData;
            this.notifyDataSetChanged();
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.tx_item_calendar_month_item, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            TXMonthModel model = listData.get(position);
            final TXDate data = model.month.date;

            int month = model.month.date.getMonth();

            holder.tvContent.setText(String.format("%1$d月", month + 1));

            if (model.month.isSelected) {
                holder.tvContent.setTextColor(Color.BLUE);
            } else {
                holder.tvContent.setTextColor(Color.BLACK);
            }

            if (model.month.isShowTodayMark) {
                holder.markView.setVisibility(View.VISIBLE);
            } else {
                holder.markView.setVisibility(View.GONE);
            }

            holder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectDateRangeListener != null) {
                        selectDateRangeListener.onSelectDate(data);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listData == null ? 0 : listData.size();
        }
    }

    private static class MyHolder extends RecyclerView.ViewHolder {

        public TextView tvContent;
        public View markView;

        public MyHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            markView = itemView.findViewById(R.id.today_mark);
        }
    }
}
