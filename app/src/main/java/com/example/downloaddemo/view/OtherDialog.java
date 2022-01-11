package com.example.downloaddemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.downloaddemo.R;

public class OtherDialog extends Dialog {
    public OtherDialog(@NonNull Context context) {
        super(context);
    }

    public OtherDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_other);
    }
}
