package com.cc.custom.calender.demo.cell;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.custom.R;
import com.cc.custom.calender.demo.listener.TXOnSelectDateListener;
import com.cc.custom.calender.demo.model.TXCalendarDayModel;
import com.cc.custom.calender.demo.model.TXCalendarMonthModel;
import com.tx.listview.base.cell.TXBaseListCell;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/28.
 */
public class TXCalendarWeekCell implements TXBaseListCell<TXCalendarMonthModel> {

    private TextView mTvTitle;
    private MyAdapter mAdapter;

    public TXCalendarWeekCell(TXOnSelectDateListener selectDateRangeListener) {
        this.mAdapter = new MyAdapter(selectDateRangeListener);
    }

    @Override
    public void setData(TXCalendarMonthModel model) {
        if (model == null) {
            return;
        }

        int year = model.month.day.getYear();
        int month = model.month.day.getMonth();

        mTvTitle.setText(String.format("%1$d年%2$d月", year, month + 1));

        mAdapter.setListData(model.dayList);
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_calendar_week;
    }

    @Override
    public void initCellViews(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), 7));
        rv.setAdapter(mAdapter);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<TXCalendarDayModel> listData;
        private TXOnSelectDateListener selectDateRangeListener;

        public MyAdapter(TXOnSelectDateListener selectDateRangeListener) {
            this.selectDateRangeListener = selectDateRangeListener;
        }

        public void setListData(List<TXCalendarDayModel> listData) {
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
            final TXCalendarDayModel model = listData.get(position);
            if (model.day == null) {
                holder.tvContent.setText(null);
                holder.markView.setVisibility(View.GONE);
                holder.tvContent.setOnClickListener(null);
                return;
            }

            int day = model.day.getDay();

            holder.tvContent.setText(String.valueOf(day));

            if (model.isSelected) {
                holder.tvContent.setTextColor(Color.BLUE);
            } else {
                holder.tvContent.setTextColor(Color.BLACK);
            }

            if (model.isShowTodayMark) {
                holder.markView.setVisibility(View.VISIBLE);
            } else {
                holder.markView.setVisibility(View.GONE);
            }

            holder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectDateRangeListener != null) {
                        selectDateRangeListener.onSelectDate(model.day);
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
