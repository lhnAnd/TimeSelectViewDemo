package com.example.downloaddemo.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.downloaddemo.event.MessageEvent;
import com.example.downloaddemo.event.MessageEvent2;

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
        ShengdanshuActivity.startActivity(this);
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
        LogActivity.startActivity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //eventBus 使用

        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
        ButterKnife.bind(this);
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
}
