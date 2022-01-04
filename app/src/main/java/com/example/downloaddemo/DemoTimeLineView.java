package com.example.downloaddemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.downloaddemo.R;
import com.example.downloaddemo.event.MessageEvent2;
import com.example.downloaddemo.util.LogUtil;
import com.example.downloaddemo.view.CustomerScrollView;
import com.example.downloaddemo.view.DemoView2;

import org.greenrobot.eventbus.EventBus;

/**
 * 2021.11 lhn
 * 时间选择器自定义view，包含一个可拖动的选择框和一个可滚动的时间轴
 * 目前不支持横屏
 */
public class DemoTimeLineView extends RelativeLayout {
    private static final String TAG = DemoTimeLineView.class.getSimpleName();
    private CustomerScrollView scrollView;
    private DemoView2 demoView2;
    private TextView tvTime;
    /**
     * 组件所选时间的最大长度
     */
    private int maxLength;
    /**
     * 组件所选时间段的最小长度
     */
    private int minLength;

    /**
     * 设置最大长度
     * @param maxLength 最大长度，单位是像素
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        if (demoView2 != null){
            demoView2.setMaxWidth(maxLength);
        }
    }

    /**
     * 设置最小长度
     * @param minLength 最小长度，单位是像素
     */
    public void setMinLength(int minLength) {
        this.minLength = minLength;
        if (demoView2!=null){
            demoView2.setMinDistance(minLength);
        }
    }

    public DemoTimeLineView(Context context) {
        super(context);
        init();
    }

    public DemoTimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 开启一个线程模拟播放
     */
    public void startRun(){
        new Thread(){
            @Override
            public void run() {
                LogUtil.d("TEST","run()");
                demoView2.setOver(false);
                while (!demoView2.isOver()){
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    demoView2.drawPointer(false);
                }
            }
        }.start();
    }

    /**
     * 刷新选中时间段
     * @param xLeft 左边选择按钮的x坐标
     * @param xRight 右边选择按钮的x坐标
     */
    @SuppressLint("SetTextI18n")
    private void resetSelectTime(int xLeft, int xRight){
        tvTime.setText(scrollView.calculSelectTime(xLeft) + "-" + scrollView.calculSelectTime(xRight));
    }

    /**
     * 初始化视图
     */
    private void init(){
        Log.d(TAG,"init()");
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.demo_download_time_line_view, null);
        demoView2 = contentView.findViewById(R.id.demo_view2);
        demoView2.setOnRightMoveListener(new DemoView2.OnMoveListener() {
            @Override
            public void onMove(int xLeft, int xRight) {
                LogUtil.d(TAG,"onMove()");
                if (scrollView != null){
                    int xScroll = scrollView.getScrollX();
                    resetSelectTime(xScroll + xLeft + demoView2.BUTTON_WIDTH, xScroll + xRight);
                }
            }
        });
        tvTime = contentView.findViewById(R.id.tv_time);
        scrollView = contentView.findViewById(R.id.scroll_view);
        scrollView.initScroll();
        scrollView.scrollTo(0, scrollView.getScrollY());
        scrollView.init();
        scrollView.startScroll(true);
        scrollView.setOnScrollTimeChangeListener(new CustomerScrollView.OnScrollTimeChangeListener() {
            @Override
            public void onScrollTimeChange() {
                LogUtil.d(TAG,"onScrollTimeChange()");
                int xScroll = scrollView.getScrollX();
                resetSelectTime( xScroll + demoView2.getxLeftButton() + demoView2.BUTTON_WIDTH, xScroll + demoView2.getxRightButton());
            }
        });
        addView(contentView);
    }
}
