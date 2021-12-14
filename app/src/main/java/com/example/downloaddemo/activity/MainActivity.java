package com.example.downloaddemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.downloaddemo.R;
import com.example.downloaddemo.DemoTimeLineView;
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

    @OnClick({R.id.demo_button_kt})
    void toKt(){
        KtTestActivity.startActivity(this);
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
        bnReceive.setText(msg.getMessage());
        Log.d(TAG,"" + msg.getNum());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
