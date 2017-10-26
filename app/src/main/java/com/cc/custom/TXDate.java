package com.cc.custom;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hyj on 11/5/15.
 */
public class TXDate implements Serializable {

    private static final String YMD_HM = "yyyy-MM-dd HH:mm";
    private static final String YMD = "yyyy-MM-dd";
    private static final String YM = "yyyy-MM";
    private static final String HM = "HH:mm";
    private static final String MD_ZH = "MM月dd日";
    private static final String YMD_HMS = "yyyy/MM/dd HH:mm:ss";

    private static final String YYYYM = "yyyy年M月";
    private static final String YYYYMD = "yyyy/M/d";
    private static final String YYYYMDHHMM = "yyyy/M/d HH:mm";
    private static final String YYMD_ZH = "yy年M月d日";

    // 新增的样式
    private static final String YYYY = "yyyy年";
    private static final String YYYY_M_D = "yyyy.M.d";
    private static final String M_D = "M.d";

    private long milliseconds;

    public TXDate(Date date) {
        if (date == null) {
            return;
        }
        this.milliseconds = date.getTime();
    }

    public TXDate(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public Date getDate() {
        return new Date(this.milliseconds);
    }

    public long getMilliseconds() {
        return this.milliseconds;
    }

    @Deprecated
    public String formatDateYMDHMS() {
        return format(YMD_HMS, this.milliseconds);
    }

    /**
     * 2015-12-23 07:30
     *
     * @return
     */
    @Deprecated
    public String formatDateYMDHM() {
        return format(YMD_HM, this.milliseconds);
    }

    /**
     * 2015-12-23
     *
     * @return
     */
    @Deprecated
    public String formatDateYMD() {
        return format(YMD, this.milliseconds);
    }

    @Deprecated
    public String formatDateYM() {
        return format(YM, this.milliseconds);
    }

    @Deprecated
    public String formatDateHM() {
        return format(HM, this.milliseconds);
    }

    // 8月15日
    @Deprecated
    public String formatDateMDZH() {
        return format(MD_ZH, this.milliseconds);
    }

    public boolean after(TXDate date) {
        return this.milliseconds > date.getMilliseconds();
    }

    public boolean after(Date date) {
        return this.milliseconds > date.getTime();
    }

    public boolean before(TXDate date) {
        return this.milliseconds < date.getMilliseconds();
    }

    public boolean before(Date date) {
        return this.milliseconds < date.getTime();
    }

    private String format(String type, long milliseconds) {
        return new SimpleDateFormat(type, Locale.getDefault()).format(new Date(milliseconds));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TXDate txDate = (TXDate) o;

        return milliseconds == txDate.milliseconds;

    }

    @Override
    public int hashCode() {
        return (int) (milliseconds ^ (milliseconds >>> 32));
    }

    /**
     * 规则一
     * 格式yyyy年m月  如2016年1月
     *
     * @return
     */
    public String formatYYYYM() {
        return format(YYYYM, this.milliseconds);
    }

    /**
     * 规则二
     * 格式yyyy/m/d  如2016/8/12
     *
     * @return
     */
    public String formatYYYYMD() {
        return format(YYYYMD, this.milliseconds);
    }

    /**
     * 规则三
     * 格式yyyy/m/d HH:mm 如2016/8/12 08:09
     *
     * @return
     */
    public String formatYYYYMDHHMM() {
        return format(YYYYMDHHMM, this.milliseconds);
    }

    /**
     * 规则四
     * 年月日星期 (特殊年月日)
     * 如 11月30日 今天 、11月19日 周六、15年11月19日 周四
     *
     * @return
     */
    public String formatYYMDWeek() {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliseconds);

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(new Date().getTime());

        //今天
        if (time.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && time.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && time.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            SimpleDateFormat sdf = new SimpleDateFormat("M月d日 今天");
            return sdf.format(time.getTime());
        } else if (time.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            SimpleDateFormat sdf = new SimpleDateFormat("M月d日 ");
            return sdf.format(time.getTime()) + formatWeek(getWeek(time));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yy年M月d日 ");
            return sdf.format(time.getTime()) + formatWeek(getWeek(time));
        }
    }

    /**
     * 规则五
     * 时间轴时间
     * 一周内时间显示 周X hh:mm 其他时间显示yy/M/dd HH:mm
     * 如16/11/27 09:37、周一 09:37、周二 09:37、昨天 09:37、今天 09:37、明天 09:37、周六 09:37、周日 09:37、16/12/05 09:37
     *
     * @return
     */
    public String formatTimeline() {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliseconds);

        //今天
        if (isToday(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
            return sdf.format(time.getTime());
        }

        //昨天
        if (isYesterday(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
            return sdf.format(time.getTime());
        }

        //明天
        if (isTomorrow(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("明天 HH:mm");
            return sdf.format(time.getTime());
        }

        int thisWeek = getThisWeek(time);
        if (thisWeek > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm");
            return formatWeek(getWeek(time)) + sdf.format(time.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yy/M/d HH:mm");
            return sdf.format(time.getTime());
        }
    }

    /**
     * 规则六
     * 会话框时间
     * 一周内时间显示 周X hh:mm 其他时间显示yyyy/M/dd HH:mm
     * 如2016/11/27 09:37、周一 09:37、周二 09:37、昨天 09:37、今天 09:37、明天 09:37、周六 09:37、周日 09:37、2016/12/05 09:37
     *
     * @return
     */
    public String formatConversationTime() {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliseconds);

        //今天
        if (isToday(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
            return sdf.format(time.getTime());
        }

        //昨天
        if (isYesterday(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
            return sdf.format(time.getTime());
        }

        //明天
        if (isTomorrow(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("明天 HH:mm");
            return sdf.format(time.getTime());
        }

        int thisWeek = getThisWeek(time);
        if (thisWeek > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm");
            return formatWeek(getWeek(time)) + sdf.format(time.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d HH:mm");
            return sdf.format(time.getTime());
        }
    }

    /**
     * 规则七
     * 消息列表时间戳
     * 一周内时间显示 周X 其他时间显示yy/M/dd
     * 如16/11/27、周一、周二 、昨天、 09:37
     *
     * @return
     */
    public String formatNewsTime() {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milliseconds);

        //今天
        if (isToday(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(time.getTime());
        }

        //昨天
        if (isYesterday(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("昨天");
            return sdf.format(time.getTime());
        }

        int thisWeek = getThisWeek(time);
        if (thisWeek > 0) {
            return formatWeek(getWeek(time));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
            return sdf.format(time.getTime());
        }
    }

    /**
     * 规则八
     * 时间段
     * 12:23-14:54
     *
     * @param toTime
     * @return
     */
    public String formatTimeRange(TXDate toTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(milliseconds);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        toCalendar.setTimeInMillis(toTime.milliseconds);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.format(calendar.getTime()) + "-" + sdf.format(toCalendar.getTime());

    }

    /**
     * 规则九，返回16年9月28的时间格式
     * @return
     */
    public String formatYYMDZH() {
        return format(YYMD_ZH, this.milliseconds);
    }

    /**
     * 格式yyyy年  如2017年
     */
    public String formatYYYY() {
        return format(YYYY, this.milliseconds);
    }

    /**
     * 格式yyyy.M.d  如2017.8.17
     */
    public String formatYYYY_M_D() {
        return format(YYYY_M_D, this.milliseconds);
    }

    /**
     * 格式M.d  如8.17
     */
    public String formatM_D() {
        return format(M_D, this.milliseconds);
    }

    /**
     * 周样式：如 2017年第40周 10.2-10.8
     */
    public String formatYYYYWeekRange() {
        Calendar time = Calendar.getInstance();
        time.setFirstDayOfWeek(Calendar.MONDAY);
        time.setMinimalDaysInFirstWeek(7);
        time.setTimeInMillis(milliseconds);
        int weekIndex = time.get(Calendar.WEEK_OF_YEAR);

        time.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        TXDate firstDay = new TXDate(time.getTimeInMillis());
        time.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        TXDate lastDay = new TXDate(time.getTimeInMillis());
        return formatYYYY() + "第" + weekIndex + "周 " + firstDay.formatM_D() + "-" + lastDay.formatM_D();
    }

    /**
     * 返回传入时间是本周内的周几
     *
     * @return
     */
    public int getThisWeek(Calendar time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -6);
        } else {
            calendar.add(Calendar.DATE, Calendar.MONDAY - week);
        }
        for (int i = 1; i <= 7; i++) {
            if (time.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && time.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && time.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                return getWeek(time);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return -1;
    }

    private boolean isToday(Calendar c) {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(new Date().getTime());
        if (c.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && c.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && c.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isYesterday(Calendar c) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        if (c.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)
                && c.get(Calendar.MONTH) == yesterday.get(Calendar.MONTH)
                && c.get(Calendar.DAY_OF_MONTH) == yesterday.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTomorrow(Calendar c) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, 1);
        if (c.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && c.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && c.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            return false;
        }
    }

    public int getWeek(Calendar c) {
        boolean isFirstSunday = (c.getFirstDayOfWeek() == Calendar.SUNDAY);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);

        //若一周第一天为星期天，则-1
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        return weekDay;
    }

    /**
     * 获取某天所在周的星期 一
     * @return
     */
    public long getFirstDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.milliseconds);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -6);
            return calendar.getTimeInMillis();
        } else {
            calendar.add(Calendar.DATE, Calendar.MONDAY - week);
            return calendar.getTimeInMillis();
        }
    }

    public String formatWeek(int week) {
        String weekStr = "";
        switch (week) {
            case 1:
                weekStr = "周一";
                break;
            case 2:
                weekStr = "周二";
                break;
            case 3:
                weekStr = "周三";
                break;
            case 4:
                weekStr = "周四";
                break;
            case 5:
                weekStr = "周五";
                break;
            case 6:
                weekStr = "周六";
                break;
            case 7:
                weekStr = "周日";
                break;
            default:
                weekStr = "";
                break;
        }
        return weekStr;
    }

    /**
     * 获取年
     */
    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月
     * <p>
     * Note: 月份和 {@link Calendar}中月份对应，如：十月，getMonth = 9
     */
    public int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取日
     */
    public int getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public String toString() {
        return "TXDate{" +
                "milliseconds=" + milliseconds +
                ", format=" + formatYYYYMDHHMM() +
                '}';
    }
}
