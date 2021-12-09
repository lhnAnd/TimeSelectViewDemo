package com.example.downloaddemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeRuleUtil {
    private static final String TAG = "TimeRuleUtil";
    public static long covertSecond2Milli(long second) {
        return second * 1000;
    }

    /**
     * 时间戳转换成字符串
     */
     public static String getDateToString(long time) {
        Date d = new Date(time);
         String s =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
         return s;
     }

    /**
     * 时间戳转换成每天的秒数，忽略天以上单位
     */
    public static int getDateToSec(long time) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        return hour * 3600 + min * 60 + sec;
    }


    public static boolean isToday(int y,int m,int d){
        Calendar calendar = Calendar.getInstance();
        int Y = calendar.get(Calendar.YEAR);
        int M = calendar.get(Calendar.MONTH) + 1;
        int D = calendar.get(Calendar.DAY_OF_MONTH);
        return y == Y && m == M && d == D;
    }

    /**
     * 获取标准的日期格式 如：2019-7-1  转换为：2019-07-01
     * */
    public static String getStandardDate(String str){
        if(str == null || str.length() < 6 || !str.contains("-")){
            return "";
        }
        String [] strArray = str.split("-");
        if(strArray.length != 3){
            return "";
        }
        int year = Integer.valueOf(strArray[0]);
        int month = Integer.valueOf(strArray[1]);
        int day = Integer.valueOf(strArray[2]);

        String result = "" + year + "-";
        if(month < 10){
            result = result + "0" + month + "-";
        }else {
            result = result  + month + "-";
        }

        if(day < 10){
            result = result + "0" + day ;
        }else {
            result = result  + day;
        }
        return result;
    }

    /**
     * 获取当前系统日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 根据给的日期，获取前或后 dayIndex 天的日期
     *
     * dayIndex 为正，则为往前日期；为负，则为往后日期
     * */
    public static String getLastDay(String time,int dayIndex){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date=null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day=calendar.get(Calendar.DATE);
        //                      此处修改为+1则是获取后一天
        calendar.set(Calendar.DATE,day - dayIndex);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }


    /**
     * 计算日期的下一天
     *
     * @param date
     * @return
     */
    public static Date getNextdayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 秒转时间 a integer to xxx'xx''
     * @param time
     * @return
     */
    public static String secToTimeStr(int time) {
        String timeStr = "00'00''";
        if(time >= 86400 || time <= 0){
            //产品的特殊处理。当获取的时长小于等于0或超过一天的秒数时，默认为显示8秒
            return "00'08''";
        }else if(time > 0) {
            timeStr = unitFormat(time / 60) + "'" + unitFormat(time % 60) + "''";
        }
        return timeStr;
    }

    /**
     * 秒转时间 a integer to xx:xx:xx
     * @param time
     * @return
     */
    public static String secToTime(int time) {

        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0 || time == 86400)//24：00显示00：00防止播放24点的视频有问题
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
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

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static void main(String arg[]){
        String nextDay = getLastDay("" + 2020 + "-" + 6+"-"+30,-1);

    }

    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int am_pm = calendar.get(Calendar.AM_PM);
        int hour   = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        if (am_pm == Calendar.PM) {
            hour += 12;
        }

        return hour * 3600 + minute * 60 + second;
    }

    public static boolean isToday(long alartTime) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(alartTime);

        Calendar todayCalender = Calendar.getInstance(Locale.getDefault());

        if (calendar.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == todayCalender.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) == todayCalender.get(Calendar.DATE)) {
            return true;
        }

        return false;
    }
}
