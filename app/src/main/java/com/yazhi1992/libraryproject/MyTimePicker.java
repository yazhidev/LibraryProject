package com.yazhi1992.libraryproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.yazhi1992.yazhilib.utils.DateUtils;
import com.yazhi1992.yazhilib.widget.LoopView.LoopView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by zengyazhi on 17/5/27.
 */

public class MyTimePicker implements View.OnClickListener {
    private View mContenView;
    private Context mContext;
    protected LoopView lv_day;
    protected LoopView lv_hours;
    protected LoopView lv_mins;
    private int mDataYear; //当前时间年
    private int mDataMonth; //当前时间月，4月=3
    private int mDataDay; //当前时间日
    private String mChoosedDay;
    int year, month, day, hours, minute; //当前选中的年
    protected ArrayList<String> mYearList;
    protected ArrayList<String> mMonthList;
    protected ArrayList<String> mDayList;
    private List<String> mList_big;
    private List<String> mList_little;
    static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG_SURE = "sure";
    private static final String TAG_CANCEL = "cancel";
    private OnTimePickerClickListener mListener;
    private Calendar nowCalendar;

    public MyTimePicker(Context context) {
        mContext = context;
        mContenView = LayoutInflater.from(mContext).inflate(R.layout.layout_wheelview, null, false);
        View sure = mContenView.findViewById(R.id.tv_sure);
        sure.setOnClickListener(this);
        sure.setTag(TAG_SURE);
        mContenView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        View cancel = mContenView.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);
        cancel.setTag(TAG_CANCEL);

        setData();
    }

    public void setOnPickerClickListener(OnTimePickerClickListener listener) {
        mListener = listener;
    }

    public interface OnTimePickerClickListener {
        void positive(int year, int month, int day, int hour, int min, String dayAndDayInWeek);

        void passive();
    }

    private void setData() {
        if (mContenView == null) return;
        lv_day = (LoopView) mContenView.findViewById(R.id.loopView3);
        lv_hours = (LoopView) mContenView.findViewById(R.id.loopView4);
        lv_mins = (LoopView) mContenView.findViewById(R.id.loopView5);

        lv_day.setNotLoop();
        lv_hours.setNotLoop();
        lv_mins.setNotLoop();

        nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());
        //获取当前时间
        year = nowCalendar.get(Calendar.YEAR);
        month = nowCalendar.get(Calendar.MONTH);
        day = nowCalendar.get(Calendar.DAY_OF_MONTH);
        hours = nowCalendar.get(Calendar.HOUR_OF_DAY);
        minute = nowCalendar.get(Calendar.MINUTE);
        mDataYear = year;
        mDataMonth = month;
        mDataDay = day;
        setPicker(year, month, day, hours, minute);

//        // 添加"日"监听
//        lv_day.setListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                String chooseYear = mYearList.get(index);
//                Log.e("--", "index" + index + " chooseYear " + chooseYear + " mDataYear " + mDataYear);
//                int thisYear = Integer.valueOf(chooseYear);
//                if (thisYear != year) {
//                    year = thisYear;
//                    year = Integer.valueOf(chooseYear);
//                    if (index == 0) {
//                        //今年，则月份要剔除已经过去的月和日
//                        //从当月开始
//                        mMonthList.clear();
//                        for (int i = mDataMonth; i < 12; i++) {
//                            mMonthList.add(String.valueOf(i + 1));
//                        }
//                        lv_month.setItems(mMonthList);
//
//                        int endDay = getEndDay(Integer.valueOf(chooseYear), mDataMonth);
//                        mDayList.clear();
//                        for (int i = mDataDay; i < endDay + 1; i++) {
//                            //判断周几
//                            String dayInWeek = "日 " + DateUtils.getDayInWeek(year + "-" + (month + 1) + "-" + i);
//                            mDayList.add(String.valueOf(i) + dayInWeek);
//                        }
//                        lv_day.setItems(mDayList);
//
//                        month = mDataMonth;
//                        day = mDataDay;
//                    } else {
//                        //判断大小月、闰年，从而改变当前选中月的天数
//                        mMonthList.clear();
//                        for (int i = 0; i < 12; i++) {
//                            mMonthList.add(String.valueOf(i + 1));
//                        }
//                        lv_month.setItems(mMonthList);
//                        //改变选中月天数
//                        int year = Integer.valueOf(chooseYear);
//                        //改变年后，默认置为1月
//                        int endDay = getEndDay(year, 0);
//                        mDayList.clear();
//                        for (int i = 1; i < endDay + 1; i++) {
//                            String dayInWeek = "日 " + DateUtils.getDayInWeek(year + "-" + (month + 1) + "-" + i);
//                            mDayList.add(String.valueOf(i) + dayInWeek);
//                        }
//                        lv_day.setItems(mDayList);
//                        month = 0;
//                        day = 0;
//                    }
//                    lv_month.setCurrentPosition(0);
//                    lv_day.setCurrentPosition(0);
//                }
//
//            }
//        });
//
//        lv_month.setListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                String monthStr = mMonthList.get(index);
//                int thisMonth = Integer.valueOf(monthStr) - 1;
//                if (month != thisMonth) {
//                    month = thisMonth;
//                    int endDay = getEndDay(year, month);
//                    mDayList.clear();
//                    int beginDay = 1;
//                    if (year == mDataYear && month == mDataMonth) {
//                        //当月，剔除已过去天数
//                        beginDay = mDataDay;
//                    }
//                    for (int i = beginDay; i < endDay + 1; i++) {
//                        String dayInWeek = "日 " + DateUtils.getDayInWeek(year + "-" + (month + 1) + "-" + i);
//                        mDayList.add(String.valueOf(i) + dayInWeek);
//                    }
//                    lv_day.setItems(mDayList);
//                    lv_day.setCurrentPosition(0);
//                    day = 0;
//                }
//            }
//        });

    }

    public void setPicker(final int year, final int month, int day, int h, int m) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        mList_big = Arrays.asList(months_big);
        mList_little = Arrays.asList(months_little);

        // 日
        // 5月是4

        Calendar calendar = nowCalendar;
        mDayList = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            nowCalendar.add(Calendar.DAY_OF_MONTH, 1);
            Date time = nowCalendar.getTime();
            String format = ymdFormat.format(time);
            String dayInWeek = DateUtils.getDayInWeek(format);
            String substring = format.substring(5);
            String dayStr = substring.replace("-", "月") + "日 " + dayInWeek;
            StringBuffer stringBuffer = new StringBuffer(dayStr);
            if (dayStr.substring(0, 1).equals("0")) {
                //去除月份中的0
                stringBuffer.setCharAt(0, ' ');
            }
            if (dayStr.substring(3, 4).equals("0")) {
                //去除日期中的0
                stringBuffer.deleteCharAt(3);
                stringBuffer.insert(0, " ");
            }
            Log.e("-", stringBuffer.toString());
            mDayList.add(dayStr);
        }

        int endDay = getEndDay(year, month);
        mDayList = new ArrayList<>();
        for (int i = day; i < endDay + 1; i++) {
            String dayInWeek = "日 " + DateUtils.getDayInWeek(year + "-" + (month + 1) + "-" + i);
            mDayList.add(String.valueOf(i) + dayInWeek);
        }
        lv_day.setItems(mDayList);

        //时
        ArrayList<String> hourList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourList.add(String.valueOf(i));
        }
        lv_hours.setItems(hourList);

        //分
        ArrayList<String> minList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minList.add(String.valueOf(i));
        }
        lv_mins.setItems(minList);

    }

    /**
     * 该年该月有几天
     *
     * @param year
     * @param month 4月则传3
     * @return
     */
    private int getEndDay(int year, int month) {
        int endDay;
        if (mList_big.contains(String.valueOf(month + 1))) {
            endDay = 31;
        } else if (mList_little.contains(String.valueOf(month + 1))) {
            endDay = 30;
        } else {
            //闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                endDay = 29;
            } else {
                endDay = 28;
            }
        }
        return endDay;
    }


    public View getContenView() {
        return mContenView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(TAG_SURE)) {
            int selectedItem = lv_hours.getSelectedItem();
            String s = mDayList.get(lv_day.getSelectedItem());
            int i = s.indexOf("日");
            String dayStr = s.substring(0, i);
            if (mListener != null) {
                mListener.positive(year, month + 1, Integer.valueOf(dayStr), selectedItem, lv_mins.getSelectedItem(), s);
            }
        } else if (v.getTag().equals(TAG_CANCEL)) {
            if (mListener != null) {
                mListener.passive();
            }
        }
    }
}
