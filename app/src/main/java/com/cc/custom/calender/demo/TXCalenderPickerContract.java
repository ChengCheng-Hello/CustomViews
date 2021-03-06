package com.cc.custom.calender.demo;

import com.cc.custom.TXBasePresenter;
import com.cc.custom.TXBaseView;
import com.cc.custom.calender.demo.model.TXCalendarModel;

import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/9/27.
 */
public class TXCalenderPickerContract {

    public interface Presenter extends TXBasePresenter {

        /**
         * 加载日期数据
         * 
         * @param startDate 开始日期
         * @param endDate 结束日期
         */
        void loadDates(TXDate startDate, TXDate endDate);

        /**
         * 选择日期
         * 
         * @param selectedDate 选择的日期
         */
        void selectDate(TXDate selectedDate);
    }

    public interface View<T extends TXCalendarModel> extends TXBaseView<Presenter> {

        /**
         * 显示日期数据
         * 
         * @param dates 日期数据
         * @param showTopDate 置顶的日期
         */
        void showDates(List<T> dates, T showTopDate);

        /**
         * 显示选择日期完成
         *
         * @param selectedStartDate 选择的开始时间
         * @param selectedEndDate 选择的结束时间
         */
        void showSelectCompleted(TXDate selectedStartDate, TXDate selectedEndDate);
    }
}
