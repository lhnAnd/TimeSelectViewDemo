package com.example.downloaddemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.downloaddemo.R;
import com.example.downloaddemo.event.MessageEvent;
import com.example.downloaddemo.event.MessageEvent2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends Activity {
    @BindView(R.id.login_title)
    TextView textView;
    @BindView(R.id.test_event_bus)
    Button bnEventBus;
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
        bnEventBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent2(1,"eventBus______"));
            }
        });
    }

    /**
     * 实现一个后台刷新
     * @param msg 接受到的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMsg(MessageEvent2 msg){
        textView.setText(msg.getMessage());
    }
}
