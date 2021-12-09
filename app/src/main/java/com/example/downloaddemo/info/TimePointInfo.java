package com.example.downloaddemo.info;

//import com.danale.sdk.utils.LogUtil;

import android.util.Log;

import java.util.Calendar;

/**
 * Description :
 * CreateTime :2018/7/19
 *
 * @author wangjing@danale.com
 * @Editor :Administrator
 * @ModifyTime :
 * @ModifyDescription :
 * Created by Administrator on 2018/7/19.
 */

public class TimePointInfo {

    public static final int TOTAL_TIME = 24 * 3600 *1000;


    private long mRecordTime;
    private long mTotalTime;
    private long mStartTimeOfDay;
    private long mStartTime;
    private float mStartPercentage;
    private float mEndPerentage;

    private int mTimeLinePlace;
    private long mCurrentPlayTime;

    public TimePointInfo(long time){

        mStartTime = time;
        mRecordTime = 20000;

        mStartTimeOfDay = calculateTimeOfDay();
        mStartPercentage = calculateStartPercentPoint();
        mEndPerentage = calculateEndPercentPoint();

        Log.e("CLOUDRECORD","mStartTimeOfDay: " + mStartTimeOfDay + "mStartPercentage : " + mStartPercentage + "mEndPerentage : " + mEndPerentage);
    }

    private long calculateTimeOfDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mStartTime);
        return calendar.get(Calendar.HOUR_OF_DAY) * 3600 * 1000 + calendar.get(Calendar.MINUTE) * 60 *1000+ calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND);
    }

    private float calculateStartPercentPoint(){
        return (float)mStartTimeOfDay/(float)TOTAL_TIME;
    }

    private float calculateEndPercentPoint(){
        return (float)(mStartTimeOfDay + mRecordTime)/(float)TOTAL_TIME;
    }

    public float getStartPercentage(){
        return mStartPercentage;
    }

    public float getEndPercentage(){
        return mEndPerentage;
    }

    public long getStartTimeOfDay(){
        return mStartTimeOfDay;
    }

    public long getEndTimeOfDay(){
        return (mStartTimeOfDay + mRecordTime);
    }


    public void setTimeLinePlace(int timeLinePlace){
        mTimeLinePlace = timeLinePlace;
    }

    public int getTimeLinePlace(){
        return mTimeLinePlace;
    }

    public void setPlayTimeMask(long timeMask){
        mCurrentPlayTime = timeMask;
    }

    public long getPlayTimeMask(){
        return mCurrentPlayTime;
    }

    public long getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }


}
