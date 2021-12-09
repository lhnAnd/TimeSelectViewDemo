package com.example.downloaddemo.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

//import com.alcidae.foundation.logger.Log;
//import com.danale.sdk.platform.constant.cloud.RecordType;
//import com.danaleplugin.video.device.bean.CloudRecordInfo;
//import com.danaleplugin.video.util.DensityConverter;
import com.example.downloaddemo.info.CloudRecordInfo;
import com.example.downloaddemo.info.TimeAreaInfo;
import com.example.downloaddemo.info.TimePointInfo;
import com.example.downloaddemo.util.DensityConverter;

import java.util.ArrayList;

/**
 * 2021.11 lhn
 * 时间轴绘制view
 */
public class TimeAreaView extends View {

    private static final boolean VERBOSE_LOG = false;
    private static final String TAG = "TimeAreaView";
    private static final int PAINT_COLOR_ALARM_AREA = Color.parseColor(/*"#DA1D1D"*//*"#845254"*//*"#ff5c3c"*/ "#FF984F");
    private static final int PAINT_COLOR_BG = Color.parseColor("#F9F9F9");
    private static final int PAINT_COLOR_BG_DARK = Color.parseColor("#F21a1a1a");

    private static final int PAINT_COLOR_PLAN_AREA = Color.parseColor(/*"#2874ca"*//*"#5792D2"*/"#007DFF");
    private static final int PAINT_COLOR_TIME_SPLIT_LINE = /*Color.WHITE*/ Color.parseColor(/*"#36ffffff" */"#000000");
    private static final int PAINT_COLOR_TIME_SPLIT_LINE_DARK = /*Color.WHITE*/ Color.parseColor(/*"#36ffffff" */"#FFFFFFFF");

    private static final int PAINT_COLOR_TIME_SPLIT_LINE_SHORT = Color.parseColor("#66FFFFFF");
    private static final int PAINT_COLOR_TEXT = /*Color.WHITE*/ Color.parseColor(/*"#36ffffff" */"#000000");
    private static final int PAINT_COLOR_TEXT_DARK = /*Color.WHITE*/ Color.parseColor(/*"#36ffffff" */"#FFFFFFFF");

    private static final int WHITE = Color.parseColor("#ffffff");

    private static final int ORANGE = Color.parseColor(/*"#FF4081"*/"#FF984F");
    private static final int ORANGE_DARK = Color.parseColor(/*"#FF4081"*/"#4DA66301");

    private static final float STEP_SIZE = 1f;

    private static final int PAINT_ALPHA_BG = 128/*77*/;

    private Paint mAlarmAreaPaint, mBgPaint, mPlanAreaPaint, mTimeSplitLinePaint, mTextPaint, mFaceMsgAreaPaint, clipsPaint;
    private ArrayList<TimeAreaInfo> mTimeAreaInfoList;
    private int mDisplayWidth;
    private int mWidth;
    private int mHeight;
    private int mLeft;
    private int mRight;
    private float mRate;
    private int mDisplayTime;
    private Handler mHandler;
    private boolean isDrawWarnAllArea = true;
    public boolean getIsDarkTheme() {
        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
//                ToastUtil.showToast(this,"现在是正常模式");
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
//                ToastUtil.showToast(this,"现在是黑暗模式");
                return true;
            default:
                return false;
        }
    }

    public ArrayList<TimePointInfo> getmTimePointInfoArrayList() {
        return mTimePointInfoArrayList;
    }

    public void setmTimePointInfoArrayList(ArrayList<TimePointInfo> mTimePointInfoArrayList) {
        this.mTimePointInfoArrayList = mTimePointInfoArrayList;
        updateFaceMsgList();
        postInvalidate();
    }

    private boolean isLarge;

    public void setDrawWarnAllArea(boolean drawWarnAllArea) {
        isDrawWarnAllArea = drawWarnAllArea;
        postInvalidate();
    }

    private ArrayList<TimePointInfo> mTimePointInfoArrayList;

    public void setLarge(boolean large) {
        isLarge = large;
    }

    public TimeAreaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler();
        initPaint();
    }

    public TimeAreaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler();
        initPaint();
    }

    public TimeAreaView(Context context) {
        super(context);
        mHandler = new Handler();
        initPaint();
    }

    private void initPaint() {

        // 背景paint
//		mBgPaint = new Paint();
//		mBgPaint.setColor(PAINT_COLOR_BG);
//		mBgPaint.setAlpha(PAINT_ALPHA_BG);
//		mBgPaint.setStyle(Style.FILL);
//		// 抗锯齿
//		mBgPaint.setAntiAlias(true);


        /**
         * 华为插件 背景paint 30%黑
         */
        mBgPaint = new Paint();
        if (getIsDarkTheme()) {
            mBgPaint.setColor(PAINT_COLOR_BG_DARK);
        } else {
            mBgPaint.setColor(PAINT_COLOR_BG);
        }

        //mBgPaint.setAlpha(76);
        mBgPaint.setStyle(Style.FILL);
        // 抗锯齿
        mBgPaint.setAntiAlias(true);


        // 报警区域paint
        mAlarmAreaPaint = new Paint();
        mAlarmAreaPaint.setColor(PAINT_COLOR_ALARM_AREA);
        mBgPaint.setAlpha(76);
        mAlarmAreaPaint.setStyle(Style.FILL);
        mAlarmAreaPaint.setAntiAlias(true);

        //人脸消息区域
        mFaceMsgAreaPaint = new Paint();
        if (getIsDarkTheme()) {
            mFaceMsgAreaPaint.setColor(ORANGE_DARK);
        } else {
            mFaceMsgAreaPaint.setColor(ORANGE);
        }


        mFaceMsgAreaPaint.setStyle(Style.FILL);
        mFaceMsgAreaPaint.setAntiAlias(true);

        clipsPaint = new Paint();
        clipsPaint.setColor(ORANGE);
        clipsPaint.setStyle(Style.FILL);
        clipsPaint.setAntiAlias(true);
        // 正常区域paint
//		mPlanAreaPaint = new Paint();
//		mPlanAreaPaint.setColor(PAINT_COLOR_PLAN_AREA);
//		mPlanAreaPaint.setStyle(Style.FILL);
//		mAlarmAreaPaint.setAntiAlias(true);

        /**
         * 华为插件 正常区域 6% 白
         */

        /**
         * c314 正常区域 10%蓝
         */

        mPlanAreaPaint = new Paint();
        mPlanAreaPaint.setColor(PAINT_COLOR_PLAN_AREA);
        mPlanAreaPaint.setAlpha(36);
        mPlanAreaPaint.setStyle(Style.FILL);
        mAlarmAreaPaint.setAntiAlias(true);

        // 时间轴paint
        mTimeSplitLinePaint = new Paint();
        if (getIsDarkTheme()) {
            mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE_DARK);
        } else {
            mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);

        }
        mTimeSplitLinePaint.setAlpha(100);
        mTimeSplitLinePaint.setStrokeWidth(2f);
        mTimeSplitLinePaint.setAntiAlias(true);

        // 字体paint
        mTextPaint = new Paint();
        if (getIsDarkTheme()) {
            mTextPaint.setColor(PAINT_COLOR_TEXT_DARK);

        } else {
            mTextPaint.setColor(PAINT_COLOR_TEXT);


        }
        mTextPaint.setAlpha(100);
        mTextPaint.setStrokeWidth(1f);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DensityConverter.dp2px(getContext(), 12));

    }

    public void setTimeOfDisplayWidth(int displayTime) {
        mDisplayTime = displayTime;
        mRate = 24f * 10 / mDisplayTime;
        mWidth = (int) (mDisplayWidth * (mRate + STEP_SIZE));
        //Log.d("display", "displayTime = " + mDisplayTime + ", displayWIdth" + mDisplayWidth);
        setLayoutParams(new LinearLayout.LayoutParams(mDisplayWidth, getLayoutParams().height));
    }

    public void setDisplayWidth(int width) {
        mDisplayWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public void setViewLeft(int left) {
        mLeft = left;
    }

    public void setViewRight(int right) {
        mRight = right;
    }

    private ArrayList<TimeAreaInfo> mCloudCreateTimeBeforeTimeAreaInfoList = new ArrayList<>();

    public ArrayList<TimeAreaInfo> getRecordInfoList() {
        return mTimeAreaInfoList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        if (mTimeAreaInfoList != null && mTimeAreaInfoList.size() != 0) {
            drawArea(canvas, mTimeAreaInfoList);
        }
        if (clipsmTimeAreaInfoList != null && clipsmTimeAreaInfoList.size() != 0) {
            drawClipsArea(canvas, clipsmTimeAreaInfoList);
        }

        if (!isDrawWarnAllArea && mTimePointInfoArrayList != null && mTimePointInfoArrayList.size() != 0) {
            drawFaceArea(canvas, mTimePointInfoArrayList);
        }
        drawTimeLine(canvas);
        if (mBgPaint != null){
            canvas.drawRect(xLeftButton,0,xLeftButton + buttonWidth, getHeight(),mBgPaint);
            canvas.drawRect(xRightButton,0,xRightButton + buttonWidth, getHeight(),mBgPaint);
            canvas.drawRect(xLeftButton+buttonWidth,0,xRightButton, 20,mBgPaint);
            canvas.drawRect(xLeftButton+buttonWidth,getHeight()-20,xRightButton, getHeight(),mBgPaint);
        }
    }


    private ArrayList<TimeAreaInfo> clipsmTimeAreaInfoList = new ArrayList<>();


    private void drawArea(Canvas canvas, ArrayList<TimeAreaInfo> list) {

        for (TimeAreaInfo timeAreaInfo : list) {

//            if (isDrawWarnAllArea) {
//                if (timeAreaInfo.getCloudRecordInfo().getRecordType() == RecordType.PLAN_RECORD) {
//                    drawPlanArea(canvas, timeAreaInfo);
//                } else if (timeAreaInfo.getCloudRecordInfo().getRecordType() == RecordType.ALERT_RECORD) {
//                    drawAlarmArea(canvas, timeAreaInfo);
//                } else if (timeAreaInfo.getCloudRecordInfo().getRecordType() == RecordType.CLIPS) {
//                    drawClipsArea(canvas, timeAreaInfo);
//                }
//            } else {
//
//                drawPlanArea(canvas, timeAreaInfo);
//            }

        }

    }

    private void drawFaceArea(Canvas canvas, ArrayList<TimePointInfo> list) {
        for (TimePointInfo timePointInfo : list) {
            drawFaceMsgArea(canvas, timePointInfo);
        }
    }

    private void drawClipsArea(Canvas canvas, ArrayList<TimeAreaInfo> list) {
        for (TimeAreaInfo timeAreaInfo : list) {
            drawClipsArea(canvas, timeAreaInfo);
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(mLeft, 0, mLeft + (mRate + STEP_SIZE) * mDisplayWidth, mHeight, mBgPaint);
    }

    private void drawTimeText(Canvas canvas, String time, float wigth) {
        canvas.drawText(time, wigth - 30, DensityConverter.dp2px(getContext(), 30), mTextPaint);
    }

    private void drawTimeBottom(Canvas canvas) {
        canvas.drawLine(mLeft + mDisplayWidth / 2f, mHeight - 35, mLeft + mDisplayWidth / 2f + mRate * mDisplayWidth,
                mHeight - 35, mTimeSplitLinePaint);
    }


    private void drawTimeLine(Canvas canvas) {
        //		drawTimeBottom(canvas);

        if (mDisplayTime == 60) {
            for (int i = 0; i <= 24 * 6; i++) {
                float wigth;
                wigth = mDisplayWidth / 2f + mDisplayWidth / (float) 36 * i;
                if (i % 6 == 0) {
                    StringBuilder builder = new StringBuilder();

                    builder.append(i / 6);
                    builder.append(":00");
                    drawTimeText(canvas, builder.toString(), wigth);
                }


                if (i % 6 == 0) {
                    if (getIsDarkTheme()) {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE_DARK);
                    } else {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);

                    }
//				canvas.drawLine(mLeft + wigth, 0, mLeft + wigth, DensityConverter.dp2px(getContext(), 15),
//						mTimeSplitLinePaint);
                    canvas.drawLine(mLeft + wigth, getHeight(), mLeft + wigth, getHeight() - DensityConverter.dp2px(getContext(), 18),
                            mTimeSplitLinePaint);
                } else {
                    if (getIsDarkTheme()) {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE_DARK);
                    } else {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);

                    }
//				canvas.drawLine(mLeft + wigth, 0, mLeft + wigth, DensityConverter.dp2px(getContext(), 8),
//						mTimeSplitLinePaint);

                    canvas.drawLine(mLeft + wigth, getHeight(), mLeft + wigth, getHeight() - DensityConverter.dp2px(getContext(), 12),
                            mTimeSplitLinePaint);
                }
            }
        } else if (mDisplayTime == 10) {
            for (int i = 0; i <= 240; i++) {
                float wigth;
                wigth = mDisplayWidth / 2f + mDisplayWidth / (float) mDisplayTime * i;
                if (i <= 239) {
                    if (i % 10 == 0) {
//					StringBuilder builder = new StringBuilder();
//
//					builder.append(i / 10);
//					builder.append(":00");
//					drawTimeText(canvas, builder.toString(), wigth);

                        for (int j = 0; j <= 5; j++) {

                            StringBuilder builderSmall = new StringBuilder();
                            builderSmall.append(i / 10);
                            builderSmall.append(":");
                            builderSmall.append(j % 10);
                            builderSmall.append("0");


                            float smallWigth;
                            smallWigth = wigth + (mDisplayWidth * (float) 10 / (float) mDisplayTime / (float) 6 * j);
                            drawTimeText(canvas, builderSmall.toString(), smallWigth);

                            mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);

                            canvas.drawLine(mLeft + smallWigth, (float)(getHeight()*0.8), mLeft + smallWigth, (float)(getHeight()*0.8) - DensityConverter.dp2px(getContext(), 15),
                                    mTimeSplitLinePaint);


                            for (int k = 1; k < 10; k++) {
                                float shortWigth;

                                shortWigth = smallWigth + ((mDisplayWidth * (float) 10 / (float) mDisplayTime / (float) 6 / (float) 10) * k);

                                mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);
                                canvas.drawLine(mLeft + shortWigth, (float)(getHeight()*0.8), mLeft + shortWigth, (float)(getHeight()*0.8) - DensityConverter.dp2px(getContext(), 8),
                                        mTimeSplitLinePaint);
                            }

                        }
                    }
                } else {

                    StringBuilder builderSmall = new StringBuilder();
                    builderSmall.append(240 / 10);
                    builderSmall.append(":");
                    builderSmall.append("0");
                    builderSmall.append("0");


                    float smallWigth;
                    smallWigth = wigth + (mDisplayWidth * (float) 10 / (float) mDisplayTime / (float) 6 * 0);
                    drawTimeText(canvas, builderSmall.toString(), smallWigth);

                    mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);

                    canvas.drawLine(mLeft + smallWigth, getHeight(), mLeft + smallWigth, getHeight() - DensityConverter.dp2px(getContext(), 15),
                            mTimeSplitLinePaint);
                }
            }

//        }else {
//            for (int i = 0; i <= 240; i++) {
//                float wigth;
//                wigth = (mDisplayWidth / 2f + mDisplayWidth / (float) mDisplayTime * i)*3/2;
//                if (i % 10 == 0) {
//                    StringBuilder builder = new StringBuilder();
//
//                    builder.append(i / 10);
//                    builder.append(":00");
//                    drawTimeText(canvas, builder.toString(), wigth);
//                }
//
//
//                if (i % 10 == 0) {
//                    if (getIsDarkTheme()) {
//                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE_DARK);
//                    } else {
//                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);
//
//                    }
////				canvas.drawLine(mLeft + wigth, 0, mLeft + wigth, DensityConverter.dp2px(getContext(), 15),
////						mTimeSplitLinePaint);
//                    canvas.drawLine(mLeft + wigth, getHeight(), mLeft + wigth, getHeight() - DensityConverter.dp2px(getContext(), 15),
//                            mTimeSplitLinePaint);
//                } else {
//                    if (getIsDarkTheme()) {
//                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE_DARK);
//                    } else {
//                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);
//
//                    }
////				canvas.drawLine(mLeft + wigth, 0, mLeft + wigth, DensityConverter.dp2px(getContext(), 8),
////						mTimeSplitLinePaint);
//
//                    canvas.drawLine(mLeft + wigth, getHeight(), mLeft + wigth, getHeight() - DensityConverter.dp2px(getContext(), 8),
//                            mTimeSplitLinePaint);
//                }
//            }

        } else {
            for (int i = 0; i <= 24 * 6; i++) {
                float wigth;
                wigth = mDisplayWidth / 2f + mDisplayWidth / (float) 18 * i;
                if (i % 6 == 0) {
                    StringBuilder builder = new StringBuilder();

                    builder.append(i / 6);
                    builder.append(":00");
                    drawTimeText(canvas, builder.toString(), wigth);
                }


                if (i % 6 == 0) {
                    if (getIsDarkTheme()) {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE_DARK);
                    } else {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);

                    }
//				canvas.drawLine(mLeft + wigth, 0, mLeft + wigth, DensityConverter.dp2px(getContext(), 15),
//						mTimeSplitLinePaint);
                    canvas.drawLine(mLeft + wigth, getHeight(), mLeft + wigth, getHeight() - DensityConverter.dp2px(getContext(), 15),
                            mTimeSplitLinePaint);
                } else {
                    if (getIsDarkTheme()) {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE_DARK);
                    } else {
                        mTimeSplitLinePaint.setColor(PAINT_COLOR_TIME_SPLIT_LINE);

                    }
//				canvas.drawLine(mLeft + wigth, 0, mLeft + wigth, DensityConverter.dp2px(getContext(), 8),
//						mTimeSplitLinePaint);

                    canvas.drawLine(mLeft + wigth, getHeight(), mLeft + wigth, getHeight() - DensityConverter.dp2px(getContext(), 8),
                            mTimeSplitLinePaint);
                }
            }
        }

    }

    private void drawPlanArea(Canvas canvas, TimeAreaInfo timeAreaInfo) {
        canvas.drawRect(mLeft + mDisplayWidth / 2f + timeAreaInfo.getStartPercentage() * mRate * mDisplayWidth, 0,
                mLeft + mDisplayWidth / 2f + timeAreaInfo.getEndPercentage() * mRate * mDisplayWidth, mHeight,
                mPlanAreaPaint);
    }

    private void drawAlarmArea(Canvas canvas, TimeAreaInfo timeAreaInfo) {
        canvas.drawRect(mLeft + mDisplayWidth / 2f + timeAreaInfo.getStartPercentage() * mRate * mDisplayWidth, 0,
                mLeft + mDisplayWidth / 2f + timeAreaInfo.getEndPercentage() * mRate * mDisplayWidth, mHeight,
                mAlarmAreaPaint);
    }

    private void drawClipsArea(Canvas canvas, TimeAreaInfo timeAreaInfo) {
        int leftPx = (int) (mLeft + mDisplayWidth / 2f + timeAreaInfo.getStartPercentage() * mRate * mDisplayWidth);
        int rightPx = (int) (mLeft + mDisplayWidth / 2f + timeAreaInfo.getEndPercentage() * mRate * mDisplayWidth);
        int extraWidth = Math.max(rightPx - leftPx, 2); // 至少绘制2px
        if (VERBOSE_LOG) {
            Log.d(TAG, "drawClipsArea, l=" + leftPx + ", r=" + rightPx + ", w=" + extraWidth);
        }
        canvas.drawRect(leftPx, 0, leftPx + extraWidth, mHeight, clipsPaint);
    }

    private void drawFaceMsgArea(Canvas canvas, TimePointInfo timePointInfo) {
        canvas.drawRect(mLeft + mDisplayWidth / 2f + timePointInfo.getStartPercentage() * mRate * mDisplayWidth, 0,
                mLeft + mDisplayWidth / 2f + timePointInfo.getEndPercentage() * mRate * mDisplayWidth, mHeight,
                mFaceMsgAreaPaint);

    }

    private void updateFaceMsgList() {
        if (mTimeAreaInfoList != null && mTimePointInfoArrayList != null) {
            ArrayList<TimePointInfo> timePointInfos = new ArrayList<>();
            for (TimePointInfo timePointInfo : mTimePointInfoArrayList) {
//                if (timePointInfo.getmStartTime() < mTimeAreaInfoList.get(0).getCloudRecordInfo().getStartTime()) {
//                    timePointInfos.add(timePointInfo);
//                }
            }
            mTimePointInfoArrayList.removeAll(timePointInfos);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, heightMeasureSpec);
    }

    private int buttonWidth = 240;
    private int xLeftButton = 0;
    private int screenWidth = 1080;
    private int xRightButton = 1080 - 240;
    private int lastX;
    private int lastY;

//    public void updateDownView(){
//        if(xRightButton<=lastX && lastX<=xRightButton+buttonWidth){
//
//            android.util.Log.e(TAG,(xRightButton + offsetX) + "," + (xLeftButton + buttonWidth) + "," + (xRightButton + offsetX));
//            if (xRightButton + offsetX > xLeftButton + buttonWidth && xRightButton + offsetX < screenWidth-buttonWidth){
//                xRightButton = xRightButton + offsetX;
//                invalidate();
//                lastX = x;
//                lastY = y;
//            }else if (!(xRightButton + offsetX > xLeftButton + buttonWidth)){
//                xRightButton = xLeftButton + buttonWidth;
//                invalidate();
////                        lastX = xLeftButton + buttonWidth;
//            }else {
//                xRightButton = screenWidth-buttonWidth;
//                invalidate();
//                lastX = screenWidth-buttonWidth;
//            }
//        }
//    }

}
