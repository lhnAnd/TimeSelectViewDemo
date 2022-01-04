package com.example.downloaddemo.util;

import android.util.Log;

import com.example.downloaddemo.event.MessageEvent2;

import org.greenrobot.eventbus.EventBus;

public class LogUtil {
    public static void d(final String TAG,String d){
        Log.d(TAG,d);

        EventBus.getDefault().post(new MessageEvent2(1,TAG + ":" + d));
    }
    public static void e(final String TAG,String d){
        Log.e(TAG,d);
        EventBus.getDefault().post(new MessageEvent2(3,TAG + ":" + d));
    }

    public static void w(final String TAG,String d){
        Log.w(TAG,d);
        EventBus.getDefault().post(new MessageEvent2(2,TAG + ":" + d));
    }
}
