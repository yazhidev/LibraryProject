package com.yazhi1992.yazhilib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zengyazhi on 17/5/24.
 */

public class TimeUtils {

    private static String[] dayInWeekTime = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
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

}
