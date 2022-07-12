package com.example.downloaddemo.view;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.downloaddemo.R;
import com.example.downloaddemo.activity.WebActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherDialog extends Dialog {

    private final Context context;


    @OnClick(R.id.liulanqi)
        void liulanqi(){
        context.startActivity(new Intent(context, WebActivity.class));
    }

    public OtherDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public OtherDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_other);
        ButterKnife.bind(this);
    }

    private void initView(){
        setContentView(R.layout.dialog_other);
        ButterKnife.bind(this);
    }
}
