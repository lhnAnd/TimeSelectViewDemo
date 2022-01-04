package com.example.downloaddemo.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.downloaddemo.util.LogUtil;

/**
 * 本来是想要通过两个view的点击事件来实现这个效果，但是这样不好绘制中间的两条线，所以放弃
 */
public class DemoView extends View {
    public DemoView(Context context) {
        super(context);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    private int screenWidth;
    private int lastX;
    private int lastY;
    private int leftLimit = 0;
    private int rightLimit;
    private boolean isLeft = false;
    private boolean isRight = false;

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    int testWidth;


    public void setLeftLimit(int leftLimit) {
        this.leftLimit = leftLimit;
    }

    public void setLeftLimit() {
        this.leftLimit = getWidth();
    }

    public void setRightLimit(int rightLimit) {
        this.rightLimit = rightLimit;
        LogUtil.e("two","rightLimit:"+rightLimit + ",this.rightLimit:" + this.rightLimit);
    }
    public void setRightLimit() {
        this.rightLimit = screenWidth - getWidth();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        testWidth = getWidth();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = x;
                lastY = y;
                LogUtil.w("VIEW","lastX:"+ lastX + ",lastY:" + lastY+ ",screenWidth:");
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = x - lastX;
                LogUtil.w("VIEW","lastX:"+ lastX + ",lastY:" + lastY+ ",x:" + x + ",y:" + y);
//                int offsetY = y - lastY;
                // 在当前left、top、right、bottom的基础上加上偏移量
                LogUtil.w("VIEW","left:"+ (getLeft() + offsetX) + ",right:" + (getRight() + offsetX) + ",screenWidth:" + screenWidth);

                int newXLeft = Math.max(getLeft() + offsetX, leftLimit);
                int newXRight = Math.min(getRight() + offsetX, rightLimit);
                LogUtil.w("VIEW","newXLeft:"+ newXLeft + ",newXRight:" + newXRight+ ",screenWidth:" + screenWidth);
                LogUtil.w("VIEW","leftLimit:"+ leftLimit + ",rightLimit:" + rightLimit);
                if (scrollerLister!=null){
                    scrollerLister.onScroller(newXLeft, newXRight,getWidth());
                }
                if ((newXLeft > leftLimit) && (newXRight < (rightLimit))){
                    layout(newXLeft,
                            getTop(),
                            newXRight,
                            getBottom() );
                } else if (!(newXLeft > leftLimit)){
                    layout(leftLimit,
                            getTop(),
                            leftLimit + getWidth(),
                            getBottom() );
                }else{
                    layout(rightLimit-getWidth(),
                            getTop(),
                            rightLimit,
                            getBottom() );
                }

                break;
        }
        return true;
    }

    private ScrollerLister scrollerLister;

    public void setScrollerLister(ScrollerLister scrollerLister) {
        this.scrollerLister = scrollerLister;
    }

    public interface ScrollerLister{
        void onScroller(int left, int right, int width);
    }
}
