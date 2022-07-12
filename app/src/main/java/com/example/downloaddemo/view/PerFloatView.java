package com.example.downloaddemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.downloaddemo.R;
import com.example.downloaddemo.util.LogUtil;

public class PerFloatView extends ViewGroup {
    private static final String TAG = "PerFloatView";
    public Context mContext;
    private int screenWidth;
    private int lastX;
    private int lastY;
    private int leftLimit = 0;
    private int rightLimit;
    private boolean isLeft = false;
    private boolean isRight = false;

    public PerFloatView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public PerFloatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public PerFloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    void initView(){
//        setClickable(true);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.per_float, this, false);
//        addView(view);

    }

    public void show(Context context,WindowManager manager,ViewGroup view2){
        View view = LayoutInflater.from(context).inflate(R.layout.per_float,view2);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        manager.addView(view,lp);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = x;
                lastY = y;
                LogUtil.w(TAG,"lastX:"+ lastX + ",lastY:" + lastY+ ",screenWidth:");
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                LogUtil.w(TAG,"lastX:"+ lastX + ",lastY:" + lastY+ ",x:" + x + ",y:" + y);
//                int offsetY = y - lastY;
                // 在当前left、top、right、bottom的基础上加上偏移量
                LogUtil.w(TAG,"left:"+ (getLeft() + offsetX) + ",right:" + (getRight() + offsetX) + ",screenWidth:" + screenWidth);

                int newXLeft = getLeft() + offsetX;
                int newXRight = getRight() + offsetX;
                int newYTop = getTop() +offsetY;
                int newYBottom = getBottom() + offsetY;
                layout(newXLeft,newYTop,newXRight,newYBottom);
                LogUtil.w(TAG,"newXLeft:"+ newXLeft + ",newXRight:" + newXRight+ ",screenWidth:" + screenWidth);
                LogUtil.w(TAG,"leftLimit:"+ leftLimit + ",rightLimit:" + rightLimit);
                break;
        }
        return false;
    }
}
