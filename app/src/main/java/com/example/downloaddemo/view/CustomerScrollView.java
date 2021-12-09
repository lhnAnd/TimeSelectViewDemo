package com.example.downloaddemo.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

//import com.alcidae.video.plugin.R;
//import com.danale.player.Utils;
//import com.danale.sdk.platform.constant.cloud.RecordType;
//import com.danale.sdk.utils.Log;
//import com.danaleplugin.video.device.bean.CloudRecordInfo;
//import com.danaleplugin.video.widget.timeline.callback.OnControllListener;
//import com.danaleplugin.video.widget.timeline.callback.OnScrollToTimeListener;
//import com.danaleplugin.video.widget.timeline.callback.OnVideoStateOkListener;
import com.example.downloaddemo.R;
import com.example.downloaddemo.callback.OnControllListener;
import com.example.downloaddemo.callback.OnScrollToTimeListener;
import com.example.downloaddemo.info.CloudRecordInfo;
import com.example.downloaddemo.info.TimeAreaInfo;
import com.example.downloaddemo.util.Utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 滚动时间轴
 */
public class CustomerScrollView extends HorizontalScrollView implements OnVideoStateOkListener {
    private static final String TAG = CustomerScrollView.class.getSimpleName();

    //决定了时间轴的间距
    public int TIME_OF_DISPLAY = 10;

    public static final int SCROLL_STATE_SCROLL = 1;
    public static final int SCROLL_STATE_SCROLL_STOP = 2;
    public static final int SCROLL_STATE_IDEL = 3;
    public static final int TOUCH_STATE_UP = 11;
    public static final int TOUCH_STATE_DOWN = 12;
    public static final int TOUCH_STATE_OTHER = 13;

    public static final int VIDEO_STATE_IDEL = 100;
    public static final int VIDEO_STATE_PLAY = 101;
    public static final int VIDEO_STATE_PAUSE = 102;
    public static final int VIDEO_STATE_STOP = 103;
    public static final int VIDEO_STATE_PLAY_OK = 104;
    public static final int VIDEO_STATE_STOP_OK = 105;

    public static final int WATCH_TYPE_ALARM = 1;
    public static final int FRIST_TO_SCROLL = 1;

    public static final int WATCH_TYPE_NORMAL = 2;

    private Context mContext;
    private TimeAreaView mTimeAreaView;

    private OnScrollToTimeListener mScrollToTimeListener;
    private OnControllListener mControllListener;

    private Timer mPlayTimer;
    private TimerTask mPlayTimerTask;
    private TimeAreaInfo mCurrentTimeAreaInfo;
    private int fristToScroll = 0;

    private int mWidth;
    private int mLeft;
    private int mRight;
    private int mHeight;
    private int mScrollState;
    private int mTouchState;
    private int mCurrentX;
    private int mVideoState;
    private int mWatchType = WATCH_TYPE_NORMAL;
    private long lastScrollUpdate = -1;
    private long mCurrentPlayTime, mLastPlayTime, mNextPlayTime, clipsNextPlayTime, pushMsgCreTime;
    private long mWatchAlarmSelectTime;
    private float mRate;
    private boolean isPlaying;
    private boolean isAutoToScrollNearest = false;
    private boolean isAutoPlayAfterPrevStop = true;
    private boolean canScroll = true;
    private boolean isStopForPlayEnd = false;
    private boolean isDestory = false;
    public static final int TOUCH_STATE_DOUBLE = 16;

    // 播放速率,默认是1正常倍速
    private double mPlayRate = 1;

    public boolean isLarge = false;
    private long clickTime = 0;

    //构造函数
    public CustomerScrollView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CustomerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CustomerScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    /**
     * 修改时间轴刻度大小
     */
    public void changeInterval() {

        this.isLarge = !this.isLarge;
        mTimeAreaView.setLarge(this.isLarge);
        if (this.TIME_OF_DISPLAY == 10) {
            this.TIME_OF_DISPLAY = 60;
        } else if (this.TIME_OF_DISPLAY == 60) {
            this.TIME_OF_DISPLAY = 30;
        } else {
            this.TIME_OF_DISPLAY = 10;
        }
        initTimeAreaView();
    }

    //是否绘制告警区域
    public void setDrawWarnAllArea(boolean drawWarnAllArea) {
        mTimeAreaView.setDrawWarnAllArea(drawWarnAllArea);
    }

    /**
     * 设置记录列表
     * @param scrollToTimeListener
     */
//    public void setRecordInfoList(ArrayList<CloudRecordInfo> recordList, long CloudCreateTime, boolean isOpenCloud) {
//        mTimeAreaView.setRecordInfoList(recordList, CloudCreateTime, isOpenCloud);
//    }
//
//    public void setFaceInfoList(ArrayList<TimePointInfo> timePointInfos, boolean isOpenCloud) {
//        mTimeAreaView.setmTimePointInfoArrayList(timePointInfos);
//        if (mTimeAreaView.getmTimePointInfoArrayList() != null && mTimeAreaView.getmTimePointInfoArrayList().size() > 1) {
//            moveToPlayWarnMsgPlace(mTimeAreaView.getmTimePointInfoArrayList().get(mTimeAreaView.getmTimePointInfoArrayList().size() - 1), true, isOpenCloud);
//        }
//
//    }

    public void setOnscrollToTimeListener(OnScrollToTimeListener scrollToTimeListener) {
        mScrollToTimeListener = scrollToTimeListener;
    }

    public void setOnControllListener(OnControllListener onControllListener) {
        mControllListener = onControllListener;
    }

//    public ArrayList<TimeAreaInfo> getRecordInfoList() {
//        return mTimeAreaView.getRecordInfoList();
//    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.customer_srcoll_view, null);
        mTimeAreaView = (TimeAreaView) view.findViewById(R.id.timeLineLayout);
        ViewTreeObserver vb = getViewTreeObserver();
        vb.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Point sizePoint = new Point();
                wm.getDefaultDisplay().getSize(sizePoint);

                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    horizontal();
                } else {
                    vertical();
                }
            }

        });

        addView(view);
        mScrollState = SCROLL_STATE_IDEL;
        mTouchState = TOUCH_STATE_OTHER;
        mVideoState = VIDEO_STATE_IDEL;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setWidth(int x) {
        mWidth = x /*- DensityConverter.dp2px(mContext, 70f)*/;
        initTimeAreaView();
    }

    public void horizontal() {
//        mWidth = Utils.screenWidth(mContext); /*- DensityConverter.dp2px(mContext, 70f)*/;
        initTimeAreaView();
    }

    public void vertical() {
        mWidth = Utils.screenWidth(mContext); /*- DensityConverter.dp2px(mContext, 70f)*/;
        initTimeAreaView();
    }


    public void init() {
        isDestory = false;
    }


    public void initTimeAreaView() {
        Log.e(TAG,"initTimeAreaView()");
        mLeft = getLeft();
        mRight = getRight();
        mHeight = getMeasuredHeight();
        mRate = 24f * 10 / TIME_OF_DISPLAY;
        //Log.d("scrollview", "width : " + mWidth + ",  height = " + mHeight + ", ");
        mTimeAreaView.setLeft(mLeft);
        mTimeAreaView.setRight(mRight);
        mTimeAreaView.setDisplayWidth(mWidth);
        mTimeAreaView.setHeight(mHeight);
        mTimeAreaView.setTimeOfDisplayWidth(TIME_OF_DISPLAY);
        invalidate();
    }

    private OnScrollTimeChangeListener onScrollTimeChangeListener;

    public void setOnScrollTimeChangeListener(OnScrollTimeChangeListener onScrollTimeChangeListener) {
        this.onScrollTimeChangeListener = onScrollTimeChangeListener;
    }

    public interface OnScrollTimeChangeListener{
        void onScrollTimeChange();
    };

    @Override
    protected void onScrollChanged(final int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mTouchState == TOUCH_STATE_DOUBLE) {
            return;
        }
        if (onScrollTimeChangeListener != null){
            onScrollTimeChangeListener.onScrollTimeChange();
        }
        Log.e(TAG, "lastScrollUpdate =" + lastScrollUpdate);
        Log.e(TAG, "x:" + getScrollX());
        Log.e(TAG, "l:" + l);

        //如果是第一次滑动，更新状态，并开启状态监听线程
        if (lastScrollUpdate == -1) {
            postDelayed(new ScrollStateHandler(), 0);
        }
        //上次更新的时间戳
        lastScrollUpdate = System.currentTimeMillis();

        if (l < 0 || l > (mRate * mWidth)) {
            return;
        }
        calculSelectTime(l);
        if (!isAutoToScrollNearest && mVideoState != VIDEO_STATE_PLAY) {
            calculSelectTime(l);
        }

    }

    /**
     * 这个线程类用于修改本视图的滚动状态，如果上次更新的时间超过0.5秒代表滚动已经停止
     */
    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            Log.e(TAG,"run()");
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 500 || fristToScroll == FRIST_TO_SCROLL) {
                lastScrollUpdate = -1;
                mScrollState = SCROLL_STATE_SCROLL_STOP;
                Log.e(TAG, "ScrollStateHandler: interval > 500 and to play");
                judgeState();
            } else {
                if (canScroll) {
                    mScrollState = SCROLL_STATE_SCROLL;
                    Log.e(TAG, "ScrollStateHandler: interval <= 500 and SCROLL_STATE_SCROLL");
                }
                postDelayed(this, 500);
            }
        }
    }

    float startX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (this.canScroll) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
//                    mTouchState = TOUCH_STATE_UP;
//                    Log.e("TIMELINE", "touch up ");
//                    if (System.currentTimeMillis() - clickTime < 300) {
//
//                        //todo 临时方法
//                        Log.e("TIMELINE", "LARGE:  mRate = " + mRate + ";mWidth = " + mWidth + ";timeline width = " + mTimeAreaView.getWidth() + ";mCurPlaytime=" + mCurrentPlayTime + ";scrollX = " + getScrollX());
//                        changeInterval();
//
//
//                        Log.e("TIMELINE", "LARGE2 :  mRate = " + mRate + ";mWidth = " + mWidth + ";timeline width = " + mTimeAreaView.getWidth() + ";mCurPlaytime=" + mCurrentPlayTime + ";scrollX = " + getScrollX());
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(200);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                ((Activity) mContext).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        scrollTo((int) ((mCurrentPlayTime) / (24f * 3600 * 1000) * mRate * mWidth), getScrollY());
//
//                                        Log.e("TIMELINE", "LARGE3 :  mRate = " + mRate + ";mWidth = " + mWidth + ";timeline width = " + mTimeAreaView.getWidth() + ";mCurPlaytime=" + mCurrentPlayTime + ";scrollX = " + getScrollX());
//                                    }
//                                });
//                            }
//                        }).start();
//
//                        mTouchState = TOUCH_STATE_DOUBLE;
//                        Log.e("TIMELINE", "touch 2 up ");
//                    }
//                    clickTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_DOWN:
                    startX = ev.getRawX();
                    mTouchState = TOUCH_STATE_DOWN;
                    mScrollState = SCROLL_STATE_IDEL;
                    break;

                case MotionEvent.ACTION_MOVE:
//                    mWatchType = WATCH_TYPE_NORMAL;
//                    if ((mVideoState == VIDEO_STATE_PLAY || mVideoState == VIDEO_STATE_PAUSE) && Math.abs(ev.getRawX() - startX) > 10) {
//                        postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                stopScroll(VIDEO_STATE_STOP);
//                                mControllListener.onStopVideo(mCurrentPlayTime, mNextPlayTime, true);
//                                isStopForPlayEnd = false;
//
//                                Log.e("TIMELINE", "ACTION_MOVE stop ");
//
//                            }
//                        }, 0);
//                    }
//                    mTouchState = TOUCH_STATE_OTHER;
                    break;
                default:
                    mTouchState = TOUCH_STATE_OTHER;
                    break;
            }
            judgeState();
            return super.onTouchEvent(ev);
//            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.canScroll) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

//    private void calculSelectTime(int scrollX) {
//        float selectX = mWidth / 2f + scrollX;
//        long selectTimeOfSec = (long) (scrollX / (mRate * mWidth) * 24 * 3600 * 1000);
//        Log.e(TAG,"time:" + formatTime(selectTimeOfSec));
////        if (mWatchType == WATCH_TYPE_ALARM) {
////            mScrollToTimeListener.onScrollToTime(formatTime(mWatchAlarmSelectTime));
////        } else {
////            mScrollToTimeListener.onScrollToTime(formatTime(selectTimeOfSec));
////        }
//    }

    public String calculSelectTime(int scrollX) {
        float selectX = scrollX - mWidth / 2f;
        long selectTimeOfSec = (long) (selectX / (mRate * mWidth) * 24 * 3600 * 1000);
        Log.e(TAG,"time:" + formatTime(selectTimeOfSec));
//        if (mWatchType == WATCH_TYPE_ALARM) {
//            mScrollToTimeListener.onScrollToTime(formatTime(mWatchAlarmSelectTime));
//        } else {
//            mScrollToTimeListener.onScrollToTime(formatTime(selectTimeOfSec));
//        }
        return formatTime(selectTimeOfSec);
    }

    public void scrollToTime(long selectTime) {
        mWatchType = WATCH_TYPE_ALARM;
        fristToScroll = FRIST_TO_SCROLL;
        mWatchAlarmSelectTime = selectTime;
        int moveX = (int) ((float) selectTime / 24 / 3600 / 1000 * (mRate * mWidth));
        if (moveX == 0) {
            moveX = 1;
        }
        invalidate();
        scrollTo(moveX, getScrollY());
        this.mTimeAreaView.invalidate();
    }

    /**
     * 将时间戳转换为时间字符串
     * @param time 时间戳
     * @return 时间字符串
     */
    private String formatTime(long time) {
        int hour = (int) (time / 3600000);
        int min = (int) ((time % 3600000) / 60000);
        int sec = (int) (((time % 3600000) % 60000) / 1000);
        StringBuilder strBuilder = new StringBuilder();
        if (hour < 10) {
            strBuilder.append("0");
        }
        strBuilder.append(hour);
        strBuilder.append(":");
        if (min < 10) {
            strBuilder.append(0);
        }
        strBuilder.append(min);
        strBuilder.append(":");
        if (sec < 10) {
            strBuilder.append(0);
        }
        strBuilder.append(sec);
        return strBuilder.toString();
    }

    private void judgeState() {
        Log.e("zzq-timeline", "mTouchState :  " + mTouchState + "   mScrollState: " + mScrollState + "   mWatchType:  " + mWatchType);

        if ((mTouchState == TOUCH_STATE_UP && mScrollState == SCROLL_STATE_SCROLL_STOP)
                || (mScrollState == SCROLL_STATE_SCROLL_STOP && mWatchType == WATCH_TYPE_ALARM)) {
            mTouchState = TOUCH_STATE_OTHER;
            mScrollState = SCROLL_STATE_IDEL;
            Log.e(TAG, "judgeState: MoveToRunnable ： " + getScrollX());
            canScroll = false;
//            postDelayed(new MoveToRunnable(searchNearestRecord(getScrollX())), getFristToScroll() ? 0 : 500);

        }
    }

    private boolean getFristToScroll() {
        if (fristToScroll == FRIST_TO_SCROLL) {
            fristToScroll = 0;
            return true;
        }
        return false;
    }


    private class MoveToRunnable implements Runnable {
        private TimeAreaInfo mTimeAreaInfo;

        public MoveToRunnable(TimeAreaInfo timeAreaInfo) {
            mTimeAreaInfo = timeAreaInfo;
        }

        @Override
        public void run() {
            Log.e(TAG, "MoveToRunnable: run start,mScrollState = " + mScrollState);
//            if (mScrollState == SCROLL_STATE_IDEL) {
//                moveToPlayStartedPlace(mTimeAreaInfo, true);
//            }

        }
    }

//    private void moveToPlayStartedPlace(TimeAreaInfo timeAreaInfo, boolean byHand) {
//
//        //拖到时间轴走这里
//        if (timeAreaInfo == null) {
//            canScroll = true;
////            Log.e("zzq-cloud","moveToPlayStartedPlace  timeAreaInfo =  null");
//            mControllListener.onOutOfLimit();
//
//
//            return;
//        }
//        Log.e(TAG, "MoveToRunnable: moveToPlayStartedPlace timeAreaInfo = " + timeAreaInfo.getCloudRecordInfo().getStartTime());
//
//        final long pushMsgCre = timeAreaInfo.getCloudRecordInfo().getStartTime();
//        Log.e(TAG, "MoveToRunnable: moveToPlayStartedPlace pushMsgCre = " + pushMsgCre);
//
////        Log.e("zzq-cloud","moveToPlayStartedPlace  pushStartTime= " + pushMsgCre);
//        if (timeAreaInfo.getTimeLinePlace() == TimeAreaInfo.OUTSIDE_AREA) {
//            isAutoToScrollNearest = true;
//            scrollTo((int) (timeAreaInfo.getStartPercentage() * mRate * mWidth), getScrollY());
//            mScrollToTimeListener.onScrollToTime(formatTime(timeAreaInfo.getStartTimeOfDay()));
//            isAutoToScrollNearest = false;
//        }
//
//        mCurrentTimeAreaInfo = timeAreaInfo;
//
//        mLastPlayTime = mCurrentPlayTime;
//        if (mCurrentTimeAreaInfo.getTimeLinePlace() == TimeAreaInfo.INSIDE_AREA) {
//            mCurrentPlayTime = mCurrentTimeAreaInfo.getPlayTimeMask();
//
//        } else {
//            mCurrentPlayTime = mCurrentTimeAreaInfo.getStartTimeOfDay();
//        }
//
//        //Log.d("playtime", mCurrentPlayTime + ",startTime = " + mCurrentTimeAreaInfo.getCloudRecordInfo().getStartTime());
//        Log.e(TAG, "MoveToRunnable: onPlayVideo mCurrentPlayTime = " + mCurrentPlayTime);
//        canScroll = false;
//
//        if (timeAreaInfo.getCloudRecordInfo().getRecordType() == CLIPS) {
//            Log.e(TAG, "MoveToRunnable: getCloudRecordInfo().getRecordType()==CLIPS " + mCurrentPlayTime);
//            Log.e(TAG, "MoveToRunnable: getCloudRecordInfo().getRecordType()==CLIPS mLastPlayTime " + mLastPlayTime);
//            Log.e(TAG, "MoveToRunnable: getCloudRecordInfo().getRecordType()==CLIPS mNextPlayTime " + mNextPlayTime);
//            for (TimeAreaInfo timeAreaInfos : getRecordInfoList()) {
//                if (timeAreaInfos.getmStartTime() == pushMsgCre) {
//                    if (getRecordInfoList().indexOf(timeAreaInfos) < getRecordInfoList().size() - 1) {
//                        clipsNextPlayTime = getRecordInfoList().get(getRecordInfoList().indexOf(timeAreaInfos) + 1).getmStartTime();
//                    } else {
//                        clipsNextPlayTime = 0;
//                    }
//                }
//            }
//            mControllListener.onPlayVideo(mCurrentPlayTime, mLastPlayTime, clipsNextPlayTime, byHand, 1, pushMsgCre);
//        } else {
////            Log.e("zzq-cloud","moveToPlayStartedPlace  onPlayVideo mCurrentPlayTime = " + mCurrentPlayTime + "; pushMsgCret = " + pushMsgCre);
//            Log.e(TAG, "MoveToRunnable: getCloudRecordInfo().getRecordType()==CLIPS=!null " + mCurrentPlayTime);
//            mControllListener.onPlayVideo(mCurrentPlayTime, mLastPlayTime, mNextPlayTime, byHand, -1, pushMsgCre);
//        }
//
//    }

//    public void moveToPlayWarnMsgPlace(TimePointInfo timePointInfo, boolean byHand, boolean isOpenCloud) {
//        //点击消息走这里
//
//        if (timePointInfo == null) {
//            canScroll = true;
//
//            return;
//        }
//        Log.e(TAG, "moveToPlayWarnMsgPlace:  timePointInfo = " + timePointInfo.getmStartTime());
//        long pushMsgCre = timePointInfo.getmStartTime();
//        Log.e(TAG, "moveToPlayWarnMsgPlace: moveToPlayStartedPlace pushMsgCre = " + pushMsgCre);
//        mControllListener.onStopVideo(mCurrentPlayTime, mNextPlayTime, true);
//        isAutoToScrollNearest = true;
//        scrollTo((int) (timePointInfo.getStartPercentage() * mRate * mWidth), getScrollY());
//        mScrollToTimeListener.onScrollToTime(formatTime(timePointInfo.getStartTimeOfDay()));
//        canScroll = false;
//
//        isAutoToScrollNearest = false;
//
//        mWatchType = WATCH_TYPE_ALARM;
//        mWatchAlarmSelectTime = timePointInfo.getStartTimeOfDay();
//
//        mCurrentTimeAreaInfo = searchNearestRecord((int) (timePointInfo.getStartPercentage() * mRate * mWidth));
//        mLastPlayTime = mCurrentPlayTime;
//
//        if (mCurrentTimeAreaInfo != null) {
//            if (mCurrentTimeAreaInfo.getTimeLinePlace() == TimeAreaInfo.INSIDE_AREA) {
//                mCurrentPlayTime = mCurrentTimeAreaInfo.getPlayTimeMask();
//            } else {
//                mCurrentPlayTime = mCurrentTimeAreaInfo.getStartTimeOfDay();
//            }
//            int isClips = -1;
//            Log.e(TAG, "moveToPlayWarnMsgPlace: onPlayVideo mCurrentPlayTime = " + mCurrentPlayTime);
//            for (TimeAreaInfo timeAreaInfo : getRecordInfoList()) {
//                if (timeAreaInfo.getmStartTime() == pushMsgCre) {
//                    if (timeAreaInfo.getCloudRecordInfo().getRecordType() == CLIPS) {
//                        isClips = 1;
//                    } else {
//                        isClips = -1;
//                    }
//                    if (getRecordInfoList().indexOf(timeAreaInfo) < getRecordInfoList().size() - 1) {
//                        clipsNextPlayTime = getRecordInfoList().get(getRecordInfoList().indexOf(timeAreaInfo) + 1).getmStartTime();
//                    } else {
//                        clipsNextPlayTime = 0;
//                    }
//                }
//            }
//
//            mControllListener.onPlayVideo(mCurrentPlayTime, mLastPlayTime, clipsNextPlayTime, byHand, isClips, pushMsgCre);
//
//
//        } else {
//            mControllListener.onOutOfLimit();
//        }
//
//
//    }


    // 檢查云服務狀態
    //獲取開通時間， -24小時記錄成clips時間
    //點擊開通時間后的消息,走時間軸selecttime
    //點擊clips時間段,也是走時間軸selectime(播放clips方式播放)
    //點擊clips開通前, 時間軸設置new list()(輔助畫出)/不請求; 點擊消息-> 顯示圖片

//    private TimeAreaInfo getFirstWarnTimeAreaInfo() {
//        TimeAreaInfo timeAreaInfo;
//        if (mTimeAreaView.getRecordInfoList() != null) {
//            for (int i = 0; i < mTimeAreaView.getRecordInfoList().size(); i++) {
//                if (mTimeAreaView.getRecordInfoList().get(i).getCloudRecordInfo().getRecordType() == RecordType.ALERT_RECORD) {
//                    return timeAreaInfo = mTimeAreaView.getRecordInfoList().get(i);
//                }
//            }
//        }
//
//        return null;
//    }
//
//    private TimeAreaInfo getFirstTimeAreaInfo() {
//        TimeAreaInfo timeAreaInfo;
//        if (mTimeAreaView.getRecordInfoList() != null) {
//            for (int i = 0; i < mTimeAreaView.getRecordInfoList().size(); i++) {
//                return timeAreaInfo = mTimeAreaView.getRecordInfoList().get(i);
//            }
//        }
//
//        return null;
//    }

//    public void moveToFirstWarnMsgPlace() {
//        boolean byHand = true;
//        TimeAreaInfo timeAreaInfo = getFirstWarnTimeAreaInfo();
//
//        if (timeAreaInfo == null) {
//            canScroll = true;
//            return;
//        }
//        Log.e(TAG, "moveToFirstWarnMsgPlace:  timeAreaInfo = " + timeAreaInfo.getCloudRecordInfo().getStartTime());
//        long pushMsgCre = timeAreaInfo.getCloudRecordInfo().getStartTime();
//        Log.e(TAG, "moveToFirstWarnMsgPlace: moveToPlayStartedPlace pushMsgCre = " + pushMsgCre);
//        mControllListener.onStopVideo(mCurrentPlayTime, mNextPlayTime, true);
//        isAutoToScrollNearest = true;
//        scrollTo((int) (timeAreaInfo.getStartPercentage() * mRate * mWidth), getScrollY());
//        setCanScroll(false);
//
//        isAutoToScrollNearest = false;
//        mCurrentTimeAreaInfo = searchNearestRecord((int) ((timeAreaInfo.getStartPercentage()) * mRate * mWidth));
//        mLastPlayTime = mCurrentPlayTime;
//
//        if (mCurrentTimeAreaInfo.getTimeLinePlace() == TimeAreaInfo.INSIDE_AREA) {
//            mCurrentPlayTime = mCurrentTimeAreaInfo.getPlayTimeMask();
//        } else {
//            mCurrentPlayTime = mCurrentTimeAreaInfo.getStartTimeOfDay();
//        }
//
////		mCurrentPlayTime = mCurrentTimeAreaInfo.getPlayTimeMask();
//        Log.e(TAG, "moveToFirstWarnMsgPlace: onPlayVideo mCurrentPlayTime = " + mCurrentPlayTime);
//        mControllListener.onPlayVideo(mCurrentPlayTime, mLastPlayTime, mNextPlayTime, byHand, 0, pushMsgCre);
//
//    }
//
//    private TimeAreaInfo searchNearestRecord(int scrollX) {
//        float selectX = mWidth / 2f + scrollX;
//        long selectTimeOfSec = 0;
//        if (mWatchType == WATCH_TYPE_ALARM) {
//            selectTimeOfSec = mWatchAlarmSelectTime;
//            mWatchType = WATCH_TYPE_NORMAL;
//        } else {
//            selectTimeOfSec = (long) ((scrollX / (mRate * mWidth)) * 24 * 3600 * 1000);
//            Log.e(TAG, "searchNearestRecord: selectTimeOfSec = " + selectTimeOfSec);
//        }
//        ArrayList<TimeAreaInfo> list = getRecordInfoList();
//        if (list == null) {
//            return null;
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (i == 0 && selectTimeOfSec < list.get(i).getStartTimeOfDay()) {
//                list.get(0).setTimeLinePlace(TimeAreaInfo.OUTSIDE_AREA);
//                if (list.size() > 1) {
//                    mNextPlayTime = list.get(1).getStartTimeOfDay();
//                }
//                Log.e(TAG, "searchNearestRecord: list.get(0) = " + list.get(0));
//                return list.get(0);
//
//            } else if (selectTimeOfSec >= list.get(i).getStartTimeOfDay()
//                    && selectTimeOfSec <= list.get(i).getEndTimeOfDay()) {
//                list.get(i).setTimeLinePlace(TimeAreaInfo.INSIDE_AREA);
//                list.get(i).setPlayTimeMask(selectTimeOfSec);
//                if (i + 1 < list.size() - 1) {
//                    mNextPlayTime = list.get(i + 1).getStartTimeOfDay();
//                }
//
//                Log.e(TAG, "searchNearestRecord: list.get(i) = " + list.get(i));
//                return list.get(i);
//
//            } else if ((i + 1) < list.size()) {
//                if (selectTimeOfSec > list.get(i).getEndTimeOfDay()
//                        && selectTimeOfSec < list.get(i + 1).getStartTimeOfDay()) {
//                    list.get(i + 1).setTimeLinePlace(TimeAreaInfo.OUTSIDE_AREA);
//                    if (i + 1 < list.size() - 1) {
//                        mNextPlayTime = list.get(i + 2).getStartTimeOfDay();
//                    }
//                    Log.e(TAG, "searchNearestRecord: list.get(i + 1) = " + list.get(i + 1));
//                    return list.get(i + 1);
//                }
//
//            } else if ((i + 1) == list.size()) {
//                /**
//                 * when move out of last area ,return null
//                 */
//
//                return null;
//                // list.get(i).setTimeLinePlace(TimeAreaInfo.OUTSIDE_AREA);
//                // return list.get(i);
//            }
//        }
//
//        return null;
//    }

    public void setPlayRate(double rate) {
        this.mPlayRate = rate;

        if (mVideoState == VIDEO_STATE_PLAY) {
            // 当正在播放时,强行改变timer执行
            startScroll(needScr);
        } else {
            // 不做处理,自然的等待下次startScroll
        }
    }

    public void startScrollWithNoScroll(boolean needScroll) {
        needScr = needScroll;

        isPlaying = true;
        mVideoState = VIDEO_STATE_PLAY;

        if (mPlayTimer != null) {
            mPlayTimer.cancel();
            mPlayTimer = null;
        }

        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
            mPlayTimerTask = null;
        }

    }

    boolean needScr;

    /**
     * 这个应该是播放的时候自动滚动
     * @param needScroll
     */
    public void startScroll(boolean needScroll) {
        needScr = needScroll;
//        Log.e("zzq-cloud","startScroll");
        isPlaying = true;
        mVideoState = VIDEO_STATE_PLAY;

        if (mPlayTimer != null) {
            mPlayTimer.cancel();
            mPlayTimer = null;
        }
        mPlayTimer = new Timer();

        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
            mPlayTimerTask = null;
        }
        mPlayTimerTask = new TimerTask() {
            @Override
            public void run() {

                mCurrentPlayTime = mCurrentPlayTime + 1000;
                if (mCurrentTimeAreaInfo != null) {
                    if (mCurrentPlayTime > mCurrentTimeAreaInfo.getEndTimeOfDay()) {
//                        Log.e("zzq-cloud","mCurrentPlayTime = " + mCurrentPlayTime + ";mCurretTimeEndTimeOfDay = " + mCurrentTimeAreaInfo.getEndTimeOfDay());
//                        Log.e("zzq-cloud","mCurretTimeEndTimeOfDay hour = " + mCurrentTimeAreaInfo.getHour() +
//                                "; min = " + mCurrentTimeAreaInfo.getMinute() + ";sec = " + mCurrentTimeAreaInfo.getSecond() + ";mill = " + mCurrentTimeAreaInfo.getMill());
                        stopScroll(VIDEO_STATE_STOP);
                        canScroll = false;

                        isStopForPlayEnd = true;
                        mControllListener.onStopVideo(mCurrentPlayTime, mNextPlayTime, false);
                        // stop auto, stop scroll,onStop,canScroll
                    } else {
//                        Log.e("zzq-cloud","onScrollToTime formatime =   " + formatTime(mCurrentPlayTime));
                        mScrollToTimeListener.onScrollToTime(formatTime(mCurrentPlayTime));
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scrollTo((int) ((mCurrentPlayTime) / (24f * 3600 * 1000) * mRate * mWidth), getScrollY());
                            }
                        });
                    }
                }

            }
        };
        mPlayTimer.schedule(mPlayTimerTask, (long) (1000 * mPlayRate), (long) (1000 * mPlayRate));
    }

    public void destory() {
        isDestory = true;
        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
            mPlayTimerTask = null;
        }

        if (mPlayTimer != null) {
            mPlayTimer.cancel();
            mPlayTimer = null;
        }
    }


    public void stopScroll(int videoState) {
        mVideoState = videoState;

        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
            mPlayTimerTask = null;
        }

        if (mPlayTimer != null) {
            mPlayTimer.cancel();
            mPlayTimer = null;
        }
    }

    public void initScroll() {
        stopScroll(VIDEO_STATE_STOP);
        mCurrentPlayTime = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onPlayOk() {
        canScroll = true;

    }

    @Override
    public void onStopOk() {
        canScroll = true;

//        // if stop auto ,play next ,move 1px
//        if (isStopForPlayEnd && isAutoPlayAfterPrevStop && !isDestory) {
//            postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    jumpToNextVideoToPlay();
//                }
//            }, 0);
//        }
    }

//    private void jumpToNextVideoToPlay() {
//        ArrayList<TimeAreaInfo> list = getRecordInfoList();
//        if (list == null) {
//            return;
//        }
//
//        int mCurrentIndex = list.indexOf(mCurrentTimeAreaInfo);
//        if (mCurrentIndex == list.size() - 1) {
//            return;
//        }
//
//        TimeAreaInfo timeAreaInfo = list.get(mCurrentIndex + 1);
//        if (mCurrentIndex + 2 < list.size()) {
//            mNextPlayTime = list.get(mCurrentIndex + 2).getStartTimeOfDay();
////            Log.e("zzq-cloud","jumpToNextVideoToPlay mNextPlayTime, " +  list.get(mCurrentIndex + 2).getSTHour() + "; " +  list.get(mCurrentIndex + 2).getSTMinute() + "; " +
////                    list.get(mCurrentIndex + 2).getSTSecond() + "; " +  list.get(mCurrentIndex + 2).getSTMill());
//        }
//        timeAreaInfo.setTimeLinePlace(TimeAreaInfo.OUTSIDE_AREA);
//
//        moveToPlayStartedPlace(timeAreaInfo, false);
//    }
}
