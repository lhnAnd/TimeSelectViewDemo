package com.example.downloaddemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.downloaddemo.R;

public class TwoDemoView extends LinearLayout {
    private DemoView demoViewLeft;
    private DemoView demoViewRight;
    private Paint paint;
    private Canvas canvas;
    RelativeLayout relativeLayout;

    public TwoDemoView(Context context) {
        super(context);
    }

    public TwoDemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        init();
    }

    public TwoDemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        init();


    }

    void initPaint(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);//设置画笔宽度 ，单位px

    }

    void init(){
        canvas = new Canvas();
        Log.e("TwoDemoView","init()");
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.download_demo_view,null );
        demoViewLeft = contentView.findViewById(R.id.demo_view1);
        demoViewRight = contentView.findViewById(R.id.demo_view2);
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        // 方法1,获取屏幕的默认分辨率
        Log.e("VIEW","初始化");
        Display display = manager.getDefaultDisplay(); // getWindowManager().getDefaultDisplay();
        demoViewLeft.setScreenWidth(display.getWidth());
        demoViewLeft.setLeft(true);
        demoViewRight.setRight(true);
        demoViewRight.setLeftLimit();
        demoViewLeft.setRightLimit();
        demoViewRight.setScreenWidth(display.getWidth());
        demoViewRight.setRightLimit(display.getWidth());
        Log.e("two","demoView1.getWidth():"+ demoViewLeft.getWidth());
        demoViewLeft.setScrollerLister(new DemoView.ScrollerLister() {
            @Override
            public void onScroller(int left, int right,int width) {
                Log.e("two","right:" + left);
                if (right<width){
                    demoViewRight.setLeftLimit(width);
                }else {
                    demoViewRight.setLeftLimit(right);
                }
            }
        });
        demoViewRight.setScrollerLister(new DemoView.ScrollerLister() {
            @Override
            public void onScroller(int left, int right,int width ) {
                Log.e("two","left:" + left);
                if (left>display.getWidth()-width){
                    demoViewLeft.setRightLimit(display.getWidth()-width);
                }else {
                    demoViewLeft.setRightLimit(left);
                }

            }
        });
        contentView.setMinimumWidth(display.getWidth());
        addView(contentView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("two","onDraw");
    }
}
