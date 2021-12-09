package com.example.downloaddemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.downloaddemo.R;
import com.example.downloaddemo.DemoTimeLineView;

public class MainActivity extends Activity {
    private Button button;
    private DemoTimeLineView demoTimeLineView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
        button  = findViewById(R.id.demo_button);
        demoTimeLineView = findViewById(R.id.demo_time_line);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoTimeLineView.startRun();
            }
        });
    }
}
