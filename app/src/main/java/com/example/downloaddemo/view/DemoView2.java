package com.example.downloaddemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import com.example.downloaddemo.R;

/**
 * 2021.12.1 lhn
 * 时间片段选择组件
 */
public class DemoView2 extends View {

    private static final String TAG = "DemoView2";
    /**
     * 代表时间指针的偏移量
     */
    private int pointerOffset = 0;

    /**
     * 两个拖动按钮对应的bitmap
     */
    private Bitmap rightBitmap;
    private Bitmap leftBitmap;

    /**
     * 代表左右两个可拖组件的最小距离
     */
    private int minDistance = 100;

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * 代表左右两个可拖组件的最大距离
     */
    private int maxWidth = 400;

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    private int width;
    private int height;

    public DemoView2(Context context) {
        super(context);
        init();
    }

    public DemoView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DemoView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public final int BUTTON_WIDTH = 100;
    private int xLeftButton = 0;
    private int xRightButton = 300;
    private int screenWidth;
    private int lastX;
    private int lastY;
    private Paint paint;
    /**
     * 时间指针画笔
     */
    private Paint linePaint;
    private boolean isOver = false;
    public int getxLeftButton() {
        return xLeftButton;
    }

    public int getxRightButton() {
        return xRightButton;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setxLeftButton(int xLeftButton) {
        this.xLeftButton = xLeftButton;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setxRightButton(int xRightButton) {
        this.xRightButton = xRightButton;
    }

    /**
     * 初始化画笔和宽度
     */
    void init(){
        paint = new Paint();
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        // 方法1,获取屏幕的默认分辨率
        Log.e("VIEW","初始化");
        Display display = manager.getDefaultDisplay();
        screenWidth = display.getWidth();
    }

    /**
     * 刷新时间指针位置
     * @param over 时间判断是否播放完
     */
    public void drawPointer(boolean over){
        Log.e("TEST", "drawPointer");
        if (!over){
            if (pointerOffset + xLeftButton+ BUTTON_WIDTH + 2 < xRightButton){
                Log.e("TEST", "to drawPointer ");
                pointerOffset = pointerOffset  + 1;
                invalidate();
            }else {
                isOver = true;
                pointerOffset = 0;
            }
        }else {
            pointerOffset = 0;
        }
    }

    /**
     * 事件处理方法
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"onTouchEvent");
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = x;
                lastY = y;
                Log.w("DemoView2","lastX:"+ lastX + ",lastY:" + lastY+ ",screenWidth:");
                if (xLeftButton<=lastX && lastX<=xLeftButton+ BUTTON_WIDTH)
                    return true;
                return xRightButton <= lastX && lastX <= xRightButton + BUTTON_WIDTH;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                int offsetX = x - lastX;
                Log.e(TAG,"MotionEvent.ACTION_MOVE");
                //如果触摸点在左边按钮上
                if(xLeftButton <= lastX && lastX <= xLeftButton + BUTTON_WIDTH){

                    final int maxPosition = xRightButton- BUTTON_WIDTH -minDistance;
                    Log.e(TAG,"xRightButton:" + xLeftButton + ",lastX" + lastX + "," + maxPosition);
                    if (xLeftButton+offsetX<=maxPosition && (xLeftButton + offsetX) >= 0){
                        xLeftButton = xLeftButton+offsetX;
                        if (xLeftButton+offsetX < xRightButton - maxWidth - BUTTON_WIDTH){
                            xRightButton = xRightButton - (xRightButton - maxWidth - BUTTON_WIDTH -(xLeftButton+offsetX));
                        }
                        invalidate();
                        lastX = x;
                        lastY = y;
                    }else if (!(xLeftButton+offsetX<=maxPosition)){
                        xLeftButton = maxPosition;
                        invalidate();
                        lastX = xRightButton- BUTTON_WIDTH - minDistance;
                    }else {
                        xLeftButton = 0;
                        invalidate();
//                        lastX = 0;
                    }
                    return true;
                }
                //如果触摸点在右边按钮上
                if(xRightButton<=lastX && lastX<=xRightButton+ BUTTON_WIDTH){
//                    Log.e(TAG,(xRightButton + offsetX) + "," + (xLeftButton + buttonWidth) + "," + (xRightButton + offsetX));
                    final int minPosition = xLeftButton + BUTTON_WIDTH + minDistance;
                    if (xRightButton + offsetX > minPosition && xRightButton + offsetX < screenWidth- BUTTON_WIDTH){

                        xRightButton = xRightButton + offsetX;
                        if (xRightButton + offsetX >= xLeftButton + maxWidth + BUTTON_WIDTH){
                            xLeftButton = xLeftButton + (  xRightButton + offsetX - xLeftButton - maxWidth - BUTTON_WIDTH);
                        }
                        lastX = x;
                        lastY = y;
                        invalidate();
                    }else if (!(xRightButton + offsetX > minPosition)){
                        xRightButton = minPosition;
                        invalidate();
                    }else {
                        xRightButton = screenWidth- BUTTON_WIDTH;
                        invalidate();
                        lastX = screenWidth- BUTTON_WIDTH;
                    }
                    return true;
                }
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (onMoveListener!=null){
            onMoveListener.onMove(xLeftButton,xRightButton);
        }
        leftBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.select_button_left), BUTTON_WIDTH,(int)(getHeight()*0.6),false);
        rightBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.select_button_right), BUTTON_WIDTH,(int)(getHeight()*0.6),false);
        Log.e("TEST", "onDraw ");
        super.onDraw(canvas);
        if (canvas != null){
            if (paint != null){
//                canvas.drawRect(xLeftButton,(float)(getHeight()*0.2),xLeftButton + buttonWidth, (float)(getHeight()-getHeight()*0.2) ,paint);
//                canvas.drawRect(xRightButton,(float)(getHeight()*0.2),xRightButton + buttonWidth, (float)(getHeight()-getHeight()*0.2),paint);
                canvas.drawBitmap(leftBitmap,xLeftButton,getHeight()*0.2f,paint);
                canvas.drawBitmap(rightBitmap,xRightButton,getHeight()*0.2f,paint);
                canvas.drawRect(xLeftButton+ BUTTON_WIDTH,(float)(getHeight()*0.2),xRightButton, (float)(getHeight()*0.2) + 10,paint);
                canvas.drawRect(xLeftButton+ BUTTON_WIDTH,(float)(getHeight()-10-getHeight()*0.2),xRightButton, (float)(getHeight()-getHeight()*0.2),paint);
                Log.e("TEST", "to drawPointer2 ");
                canvas.drawLine(xLeftButton+ BUTTON_WIDTH +2+pointerOffset,0,pointerOffset + xLeftButton+ BUTTON_WIDTH + 2,getHeight(),linePaint);
            }
        }
    }

    private OnMoveListener onMoveListener;

    public void setOnRightMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    /**
     * 移动事件监听器回调
     */
    public interface OnMoveListener{
        void onMove(int xLeft, int xRight);
    }
}
