package com.yazhi1992.yazhilib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zengyazhi on 17/5/24.
 */

public class LibTimeUtils {

    private static String[] dayInWeekTime = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

    private LibTimeUtils() {
    }

    /**
     * 获取两个日期相隔天数
     * @param now
     * @param day2
     * @return 负数表示还距离指定日期多少天
     */
    public static int getGapBetweenTwoDay(Date now, Date day2) {
        int gap = 0;
        try {
            String formatNow = ymdFormat.format(now);
            String formatDay2 = ymdFormat.format(day2);
            Date parseNow = ymdFormat.parse(formatNow);
            Date parseDay2 = ymdFormat.parse(formatDay2);
            gap = (int) ((parseNow.getTime() - parseDay2.getTime()) / 1000 / 60 / 60 / 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gap;
    }

    public static int getDayInWeekInt(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
        if (instance.getFirstDayOfWeek() == Calendar.SUNDAY) {
            //如果一周第一天为星期天，则返回 1日，2一，3二。。以此类推
            //如果一周第一天为星期一，则返回 1一，2二，3三。。以此类推
            dayOfWeek -= 1;
            if (dayOfWeek == 0) {
                //周日
                dayOfWeek = 7;
            }
        }
        return dayOfWeek;
    }

    /**
     * 计算周几
     *
     * @param date
     * @return
     */
    public static String getDayInWeek(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
        if (instance.getFirstDayOfWeek() == Calendar.SUNDAY) {
            //如果一周第一天为星期天，则返回 1日，2一，3二。。以此类推
            //如果一周第一天为星期一，则返回 1一，2二，3三。。以此类推
            dayOfWeek -= 1;
            if (dayOfWeek == 0) {
                //周日
                dayOfWeek = 7;
            }
        }
        return dayInWeekTime[dayOfWeek];
    }

    /**
     * 计算周几
     *
     * @param date
     * @param textFormat {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"}
     * @return
     */
    public static String getDayInWeek(Date date, String[] textFormat) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
        if (instance.getFirstDayOfWeek() == Calendar.SUNDAY) {
            //如果一周第一天为星期天，则返回 1日，2一，3二。。以此类推
            //如果一周第一天为星期一，则返回 1一，2二，3三。。以此类推
            dayOfWeek -= 1;
            if (dayOfWeek == 0) {
                //周日
                dayOfWeek = 7;
            }
        }
        return textFormat[dayOfWeek];
    }


    /**
     * 计算周几
     *
     * @param time 格式：yyyy-MM-dd
     * @return
     */
    public static String getDayInWeek(String time) {
        try {
            return getDayInWeek(ymdFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 计算周几
     *
     * @param format 时间格式
     * @param time   时间字符串
     * @return
     */
    public static String getDayInWeek(SimpleDateFormat format, String time) {
        try {
            return getDayInWeek(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 根据秒换算成时间
     *
     * @param time
     * @return 00:00:00 样式
     */
    public static String changeSecondToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }


    /**
     * 根据秒换算成时间
     *
     * @param time
     * @return 120:23 样式
     */
    public static String changeSecondToTimeNoHour(int time) {
        String timeStr = null;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            second = time % 60;
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 比较两个日期的大小
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isDateOneBigger(String str1, String str2, SimpleDateFormat sdf) {
        boolean isBigger = false;
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else {
            isBigger = false;
        }
        return isBigger;
    }

}
