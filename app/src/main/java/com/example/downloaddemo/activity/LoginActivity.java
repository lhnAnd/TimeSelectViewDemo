package com.example.downloaddemo.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.annotation.RequiresApi;

import com.example.downloaddemo.R;
import com.example.downloaddemo.event.MessageEvent;
import com.example.downloaddemo.event.MessageEvent2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
@Deprecated
public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.login_title)
    TextView textView;
//    @BindView(R.id.test_event_bus)
//    Button bnEventBus;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
//        bnEventBus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new MessageEvent2(1,"eventBus______"));
//            }
//        });

    }

    private final Path mPath = new Path();
    private int touchX = 200;
    private int touchY = 200;
    private final Paint paint = new Paint();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
//        Path path = new Path();
//        path.quadTo(800, 200, 800, 800);
//        //????????????
//        ObjectAnimator animator = ObjectAnimator.ofFloat(ani, "x", "y", path);
//        animator.setDuration(4000);
//        animator.start();
//        //??????????????????
//        ObjectAnimator animator2 = ObjectAnimator.ofArgb(ani, "BackgroundColor", 0xff000000, 0x00110011, 0xffff22ff);
//        animator2.setDuration(4000);
//        animator2.start();
//        Log.e(TAG,0xff000000+",,");
//
//        Canvas canvas = new Canvas();
//        paint.setColor(Color.RED);
//        canvas.drawPath(mPath,paint);
    }

    /**
     * ????????????????????????
     * @param msg ??????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMsg(MessageEvent2 msg){
        textView.setText(msg.getMessage());
    }
}
