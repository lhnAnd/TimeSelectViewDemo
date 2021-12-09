package com.example.downloaddemo.info;

//import com.danale.sdk.platform.constant.cloud.RecordType;
//import com.danale.sdk.platform.constant.v3.message.PushMsgType;
//import com.ningerlei.timeline.constant.ColorType;
//import com.ningerlei.timeline.entity.TimeData;

import java.util.Objects;


public class CloudRecordInfo {
//	private String mDeviceId;
//	private int mChanNo;
//	private long mStartTime;
//	private long mTimeLen;
//	private long mRealTimeLen;
//	private RecordType mRecordType;
//	private PushMsgType alarmType;
//
//	private boolean isEmmcRecord;
//
//	public CloudRecordInfo() {
//		super();
//	}
//
//	public CloudRecordInfo(String deviceId, int chanNo, long startTime,
//                           long timeLen, RecordType recordType, PushMsgType alarmType) {
//		super();
//		this.mDeviceId = deviceId;
//		this.mChanNo = chanNo;
//		this.mStartTime = startTime;
//		this.mTimeLen = timeLen;
//		this.mRecordType = recordType;
//		this.startTime = startTime;
//		this.offset = mTimeLen;
//		this.alarmType = alarmType;
//		setColorType(recordType == RecordType.PLAN_RECORD ? ColorType.NORMAL : ColorType.MOTION);
//	}
//
//	public CloudRecordInfo(String deviceId, int chanNo, long startTime,
//                           long timeLen, RecordType recordType) {
//		this(deviceId, chanNo, startTime, timeLen, recordType, null);
//	}
//
//	public CloudRecordInfo(String deviceId, int chanNo, long startTime,
//                           long timeLen, RecordType recordType, boolean isEmmcRecord) {
//		this(deviceId,chanNo,startTime,timeLen,recordType);
//		this.isEmmcRecord = isEmmcRecord;
//	}
//
//	public CloudRecordInfo(String deviceId, int chanNo, long startTime,
//                           long timeLen, RecordType recordType, PushMsgType alarmType, boolean isEmmcRecord) {
//		this(deviceId,chanNo,startTime,timeLen,recordType, alarmType);
//		this.isEmmcRecord = isEmmcRecord;
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//		CloudRecordInfo that = (CloudRecordInfo) o;
//		return mStartTime == that.mStartTime &&
//				mTimeLen == that.mTimeLen &&
//				Objects.equals(mDeviceId, that.mDeviceId) &&
//				mRecordType == that.mRecordType;
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(mDeviceId, mChanNo, mStartTime, mTimeLen, mRealTimeLen, mRecordType, isEmmcRecord);
//	}
//
//	public String getDeviceId() {
//		return mDeviceId;
//	}
//
//	public void setDeviceId(String deviceId) {
//		this.mDeviceId = deviceId;
//	}
//
//	public int getChanNo() {
//		return mChanNo;
//	}
//
//	public void setChanNo(int chanNo) {
//		this.mChanNo = chanNo;
//	}
//
//	public long getStartTime() {
//		return mStartTime;
//	}
//
//	public void setStartTime(long startTime) {
//		this.mStartTime = startTime;
//		this.startTime = startTime;
//	}
//
//	public long getTimeLen() {
//		return mTimeLen;
//	}
//
//	public void setTimeLen(long timeLen) {
//		this.mTimeLen = timeLen;
//		this.offset = timeLen;
//	}
//
//	public RecordType getRecordType() {
//		return mRecordType;
//	}
//
//	public void setRecordType(RecordType recordType) {
//		this.mRecordType = recordType;
//		setColorType(recordType == RecordType.PLAN_RECORD ? ColorType.NORMAL : ColorType.MOTION);
//	}
//
//	public long getRealTimeLen() {
//		return mRealTimeLen;
//	}
//
//	public void setRealTimeLen(long mRealTimeLen) {
//		this.mRealTimeLen = mRealTimeLen;
//	}
//
//
//	public boolean isEmmcRecord() {
//		return isEmmcRecord;
//	}
//
//	public void setEmmcRecord(boolean emmcRecord) {
//		isEmmcRecord = emmcRecord;
//	}
//
//	public PushMsgType getAlarmType() {
//		return alarmType;
//	}
//
//	public void setAlarmType(PushMsgType alarmType) {
//		this.alarmType = alarmType;
//	}
//
//	@Override
//	public String toString() {
//		return "CloudRecordInfo{" +
//				"mDeviceId='" + mDeviceId + '\'' +
//				", mChanNo=" + mChanNo +
//				", mStartTime=" + mStartTime +
//				", mTimeLen=" + mTimeLen +
//				", mRealTimeLen=" + mRealTimeLen +
//				", mRecordType=" + mRecordType +
//				", isEmmcRecord=" + isEmmcRecord +
//				", startTime=" + startTime +
//				", offset=" + offset +
//				'}';
//	}
//
//	@Override
//	public int compareTo(CloudRecordInfo o) {
//		return (int) (startTime/1000 - o.startTime/1000);
//	}
}
