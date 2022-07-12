package com.example.downloaddemo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.downloaddemo.R;
import com.example.downloaddemo.DemoTimeLineView;
import com.example.downloaddemo.adapter.SelectAdapter;
import com.example.downloaddemo.data.ItemData;
import com.example.downloaddemo.db.DbUtil;
import com.example.downloaddemo.event.MessageEvent;
import com.example.downloaddemo.event.MessageEvent2;
import com.example.downloaddemo.util.LogUtil;
import com.example.downloaddemo.view.OtherDialog;
import com.example.downloaddemo.view.PerFloatView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private NotificationManager notificationManager;
    public static LogActivity logActivity;
    private SelectAdapter selectAdapter;

    @BindView(R.id.demo_button)
    Button button;

    @BindView(R.id.demo_button_2)
    Button button2;

    @BindView(R.id.demo_time_line)
    DemoTimeLineView demoTimeLineView;

    @BindView(R.id.demo_button_event_bus)
    Button bnSend;

    @BindView(R.id.demo_button_event_bus2)
    Button bnReceive;

    @BindView(R.id.to_login)
    Button bnLogin;

    @BindView(R.id.log_recycle2)
    RecyclerView recyclerView;

    @OnClick(R.id.to_login)
    void toLogin(){
        LoginForKTActivity.startActivity(this);
    }
    @OnClick(R.id.to_bottom)
    void toBottom(){
        recyclerView.getLayoutManager().scrollToPosition(0);
    }

    @OnClick({R.id.demo_button_kt})
    void toKt(){
        KtTestActivity.startActivity(this);
    }
    @OnClick({R.id.bn_clear})
    void clear(){
        selectAdapter.clear();
    }

    @OnClick({R.id.shengdanshu})
    void toShengdanshu(){
        Intent intent = new Intent(this,NewMainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.demo_button_notification)
    void sendANotification(){
        if (notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        NotificationChannel notificationChannel = new NotificationChannel("normal","Normal",NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new NotificationCompat.Builder(this,"normal")
                .setContentTitle("这是一个通知")
                .setContentText("要开心哦！！")
                .setSmallIcon(R.drawable.ic_tips01)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_tips01))
                .build();
        notificationManager.notify(1,notification);
    }

    @OnClick(R.id.ac_log)
    void toLog(){
        DbUtil dbUtil = DbUtil.getInstance(this);
//        dbUtil.createTable();
        dbUtil.insert();
        dbUtil.query();
//        LogActivity.startActivity(this);
    }

    @OnClick(R.id.other_button)
    void toOther(){
        OtherDialog otherDialog = new OtherDialog(this);
        otherDialog.setCanceledOnTouchOutside(true);
        otherDialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //eventBus 使用

        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
//        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_main_,null);
        setContentView(R.layout.activity_main_);
        ButterKnife.bind(this);
        View view = LayoutInflater.from(this).inflate(R.layout.per_float, null);
        WindowManager.LayoutParams layoutParam = new WindowManager.LayoutParams();
        layoutParam.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParam.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        getWindowManager().addView(view,layoutParam);
        view.setOnTouchListener(new ItemListener(getWindowManager(),layoutParam));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoTimeLineView.startRun();
            }
        });
        selectAdapter = new SelectAdapter(this);
        recyclerView.setAdapter(selectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startActivity(MainActivity.this);
            }
        });
        bnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent2(0,"eventBus"));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMsg(MessageEvent2 msg){
        //bnReceive.setText(msg.getMessage());
        selectAdapter.addLog(new ItemData(msg.getMessage(),msg.getNum()));
        recyclerView.getLayoutManager().scrollToPosition(selectAdapter.getItemCount()-1);
        Log.d(TAG,"" + msg.getNum());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    class ItemListener implements View.OnTouchListener {
        private int lastX;
        private int lastY;
        private int x1;
        private int y1;
        private WindowManager manager;
        private WindowManager.LayoutParams layoutParams;

        public ItemListener(WindowManager manager, WindowManager.LayoutParams layoutParams) {
            this.manager = manager;
            this.layoutParams = layoutParams;
            x1 = layoutParams.x;
            y1 = layoutParams.y;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            Log.d(TAG,"action:" + event.getAction() + ",X = " + x + ",Y = " + y+",view=" + v.hashCode());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = x;
                    lastY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int offsetX = x - lastX;
                    int offsetY = y - lastY;
                    x1 = x1 + offsetX;
                    y1 = y1 + offsetY;
                    layoutParams.x = x1;
                    layoutParams.y = y1;
                    lastX = x;
                    lastY = y;
                    Log.d(TAG,"NEWX = " + layoutParams.x + ",NEWY = " + layoutParams.y);
                    manager.updateViewLayout(v, layoutParams);
                    break;
            }
            return false;
        }
    }
}
