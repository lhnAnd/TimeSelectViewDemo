package com.example.downloaddemo.info;

//import com.danale.sdk.platform.constant.cloud.RecordType;
//import com.danale.sdk.utils.LogUtil;
//import com.danaleplugin.video.device.bean.CloudRecordInfo;
//import com.danaleplugin.video.widget.timerule.TimeRuleUtil;

import com.example.downloaddemo.util.TimeRuleUtil;

import java.util.Calendar;

public class TimeAreaInfo {
	public static final int RECORD_TYPE_PLAN = 1;
	public static final int RECORD_TYPE_ALARM = 2;
	public static final int TOTAL_TIME = 24 * 3600 *1000;
	public static final int INSIDE_AREA = 4;
	public static final int OUTSIDE_AREA = 5;
	private long mRecordTime;

	public long getRealTimeLen() {
		return mRealTimeLen;
	}

	public void setRealTimeLen(long mRealTimeLen) {
		needReCalcEndSec = true;
		this.mRealTimeLen = mRealTimeLen;
	}

	private long mRealTimeLen;
	private long mTotalTime; 
	private long mStartTimeOfDay;

	public long getmStartTime() {
		return mStartTime;
	}

	public void setmStartTime(long mStartTime) {
		needReCalcEndSec = true;
		this.mStartTime = mStartTime;
	}

	private long mStartTime;
	private float mStartPercentage;
	private float mEndPerentage;
	//private Date mDateFormat;
//	private RecordType mRecordType;
//	private CloudRecordInfo mCloudRecordInfo;
	private int mTimeLinePlace;
	private long mCurrentPlayTime;

	private int startSec;//一天中的第几秒
	private int endSec;//一天中的第几秒
	private boolean needReCalcEndSec;
	
//	public TimeAreaInfo(CloudRecordInfo cloudRecordInfo){
//		needReCalcEndSec = true;
//		mStartTime = cloudRecordInfo.getStartTime();
//		mRecordTime = cloudRecordInfo.getTimeLen();
//		mRealTimeLen = cloudRecordInfo.getRealTimeLen();
//		mRecordType = cloudRecordInfo.getRecordType();
//		mCloudRecordInfo = cloudRecordInfo;
//		mStartTimeOfDay = calculateTimeOfDay();
//		mStartPercentage = calculateStartPercentPoint();
//		mEndPerentage = calculateEndPercentPoint();
//	}
	
	private long calculateTimeOfDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime);
		return calendar.get(Calendar.HOUR_OF_DAY) * 3600 * 1000
				+ calendar.get(Calendar.MINUTE) * 60 *1000
				+ calendar.get(Calendar.SECOND) * 1000
				+ calendar.get(Calendar.MILLISECOND);
	}

	public long getHour(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime + mRecordTime);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public long getSTHour(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public long getSTMinute(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime );
		return calendar.get(Calendar.MINUTE);
	}

	public long getMinute(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime + mRecordTime);
		return calendar.get(Calendar.MINUTE);
	}

	public long getSTSecond(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime );
		return calendar.get(Calendar.SECOND);
	}
	public long getSecond(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime + mRecordTime);
		return calendar.get(Calendar.SECOND);
	}
	public long getSTMill(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime);
		return calendar.get(Calendar.MILLISECOND);
	}

	public long getMill(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mStartTime + mRecordTime);
		return calendar.get(Calendar.MILLISECOND);
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
	
//	public CloudRecordInfo getCloudRecordInfo(){
//		return mCloudRecordInfo;
//	}
	
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


	public int getStartSec() {
		startSec = TimeRuleUtil.getDateToSec(mStartTime);
		return startSec;
	}

//	public long getEndSec() {
//		if (needReCalcEndSec) {
//			if(mCloudRecordInfo != null){
//				if (mCloudRecordInfo.isEmmcRecord()) {
//					endSec = TimeRuleUtil.getDateToSec(mStartTime) + (int) (mRecordTime / 1000);
//				} else if (mCloudRecordInfo.getRecordType() == RecordType.PLAN_RECORD
//						|| mCloudRecordInfo.getRecordType() == RecordType.ALERT_RECORD) {
//					// 全天云录像，告警云录像
//					endSec = TimeRuleUtil.getDateToSec(mStartTime) + (int) (mRecordTime / 1000);
////				LogUtil.d("TimeAreaInfo", "PLAN_RECORD startTime = " + mStartTime + " endSec = " + endSec + " mRecordTime = " + mRecordTime);
//				} else {
//					// CLIPS
//					endSec = (int) (TimeRuleUtil.getDateToSec(mStartTime) + mRealTimeLen);
////				LogUtil.d("TimeAreaInfo", "OTHERS startTime = " + mStartTime + " endSec = " + endSec + " mRealTimeLen = " + mRealTimeLen);
//				}
//			}
//			if ((endSec - startSec) == 0) {
//				// 对于偶现的时间错误状态，计算一个默认值保证界面能看到
//				endSec = startSec + 8;
//				LogUtil.e("TimeAreaInfo", "endSec-startSec calc == 0, set to 8, mCloudRecordInfo = " + mCloudRecordInfo);
//			}
//			if (endSec == 0) {
//				/*
//				 * fixme
//				 *  这是一个dirty fix，之所以这么处理，是因为
//				 *  在 com.danaleplugin.video.widget.timerule.TimeRuleUtil.getDateToSec 这个方法里
//				 *  忽略了跨天的存在，全天云录像和全天本地录像都会导致获取到的最后一条 Record 具有跨天的
//				 *  00:00:00 时间，在上述方法里，这会导致返回值为0，这导致在
//				 *  com/danaleplugin/video/widget/timerule/TimeRuleView.java:1812 这个位置
//				 *  com.danaleplugin.video.widget.timerule.TimeRuleView.searchNearestRecordIndex 这个方法
//				 *  内部，会进入一条分支，使代码逻辑认为目前滑动位置已经超过了当天的最后一条录像
//				 *  从而尝试跳转
//				 *
//				 *  11.25更新
//				 *  实际上，只有将录像长度也加入参数进行计算会触发这个问题，所以一个比较实际的解决方法是，
//				 *  不将录像时长加入时间戳转秒数计算中，而是单独将录像时长转换为秒数，再加到计算好的起始秒数上
//				 */
//				LogUtil.e("TimeAreaInfo", "endSec calc == 0, mCloudRecordInfo = " + mCloudRecordInfo);
////			endSec = 86399;
//			}
//			needReCalcEndSec = false;
//		}
//		return endSec;
//	}


	public int getmRecordTime() {
		return (int) (mRecordTime/1000);
	}

	public void setmRecordTime(long mRecordTime) {
		needReCalcEndSec = true;
		this.mRecordTime = mRecordTime;
	}
}
