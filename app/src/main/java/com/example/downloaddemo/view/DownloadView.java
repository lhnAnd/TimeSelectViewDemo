package com.example.downloaddemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



import androidx.annotation.Nullable;


import com.example.downloaddemo.R;

import java.util.Timer;
import java.util.TimerTask;


public class DownloadView extends View {

    private static final String TAG = "DownloadView";
    private Timer timer;
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            touchX=touchX+2;
            postInvalidate();
        }
    };
    private int progress = 0;

    public DownloadView(Context context) {
        super(context);
        init();
    }

    public DownloadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DownloadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Log.e(TAG,"init");
        mPaint.setColor(Color.RED);
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(50);
        this.setWillNotDraw(false);
        timer = new Timer();
        timer.schedule(timerTask,500,30);
    }

    private int startX,startY;
//    private final Path mPath = new Path();
    private int endX,endY,touchX,touchY,r;
    private final Paint mPaint = new Paint();
    private final Paint mPaintCircle = new Paint();
    private final Paint mPaintText = new Paint();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.e(TAG,"onMeasure");
//
//        startX = getWidth()/2;
//        startY = 0;
//        endY = getHeight();
//        endX = startX;
//        Log.e(TAG,"onMeasure,startX:" + startX + "," + (startY-endY)/2 + "," + startX+","+widthMeasureSpec+","+heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"onTouchEvent");
        touchX =(int) event.getX();
        postInvalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e(TAG,"onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        startX = w/2;
        r = startX/2;
        startY = 0;
        endX = startX;
        endY = h;
        touchX = r;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG,"onDraw");
        Log.e(TAG,"startX:" + startX + "," + (startY-endY)/2 + "," + startX+","+touchY+","+ touchX );

        mPaintCircle.setStrokeWidth(20);
        mPaintCircle.setStyle(Paint.Style.FILL);
        @SuppressLint("DrawAllocation") Path mPath = new Path();
        @SuppressLint("DrawAllocation") RectF rectF = new RectF(r,r,3*r,3*r);
        Path semicircle = new Path();
        if (startX>touchX){
            mPaint.setColor(getResources().getColor(R.color.grey));
            semicircle.arcTo(rectF,-90,180);
            mPaintCircle.setColor(getResources().getColor(R.color.black2));
            progress = 0;
        }else {
            mPaint.setColor(getResources().getColor(R.color.black2));
            semicircle.arcTo(rectF,90,180);
            mPaintCircle.setColor(getResources().getColor(R.color.grey));
            progress = 100;
        }

        mPath.moveTo(startX,(float) startX/2);
//        mPath.quadTo(touchX,touchY,startX,(float)startX*3/2);
        if (touchX<r*2/3){
            if (touchX<r*2/3){
                mPath.cubicTo((float)r*2/3,(float)startX/2,(float)r*2/3,(float)startX*3/2,startX,(float)startX*3/2);
            }else if (touchX>r*10/3){
                mPath.cubicTo((float)r*10/3,(float)startX/2,(float)r*10/3,(float)startX*3/2,startX,(float)startX*3/2);

            }timer.cancel();
        }else {
            progress = (int)(300*(touchX-r*2/3)/(r*8));
            mPath.cubicTo(touchX,(float)startX/2,touchX,(float)startX*3/2,startX,(float)startX*3/2);
        }


        canvas.drawCircle(startX,(float) -(startY-endY)/2,(float) startX/2,mPaintCircle);
        if (touchX != 0){
            canvas.drawPath(mPath,mPaint);
            canvas.drawPath(semicircle,mPaint);
        }
        canvas.drawText(progress + "%",(float) r*13/8,(float) r*17/8,mPaintText);
    }
}
