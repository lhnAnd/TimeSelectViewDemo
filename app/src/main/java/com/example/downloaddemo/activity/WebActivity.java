package com.example.downloaddemo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.downloaddemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebActivity extends Activity {
    @BindView(R.id.wv_main)
    WebView wvMain;

    @OnClick(R.id.go_back)
    void goBack(){
        if (wvMain.canGoBack()){
            wvMain.goBack();
        }
    }

    @OnClick({R.id.go_forward})
    void goForward(){
        if (wvMain.canGoForward()){
            wvMain.goForward();
        }
    }

    @BindView(R.id.tv_url)
    TextView tvUrl;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liulanqi);
        ButterKnife.bind(this);
        wvMain.loadUrl("http://www.baidu.com/");
        wvMain.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        WebSettings ws = wvMain.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setLoadsImagesAutomatically(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        tvUrl.setText(wvMain.getUrl());
        return super.onTouchEvent(event);
    }
}
