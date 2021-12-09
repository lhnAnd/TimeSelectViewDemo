package com.example.downloaddemo.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";
    public static long LOAD_TIME_TAG;
    private static HashSet<Object> huaweiCurvedDisplayHackSet;

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int screenWidth(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            if (windowMgr != null) {
                windowMgr.getDefaultDisplay().getRealMetrics(dm);
            }
            return dm.widthPixels;
        } else {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
    }

    public static int screenHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            if (windowMgr != null) {
                windowMgr.getDefaultDisplay().getRealMetrics(dm);
            }

            if (isHuaweiCurvedDisplay() && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // fixme 如果是华为瀑布屏设备，又是横屏，在获取到的物理尺寸基础上减去48像素，
                //  兼容智慧生活强制不允许插件左右扩展显示，获取到屏幕尺寸不正确的情况，
                //  未来智慧生活如果把这个改好了，或者播放器重构了，就不需要这个了
                return dm.heightPixels - 48;
            } else {
                return dm.heightPixels;
            }

        } else {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
    }

    /**
     * 智慧生活自己兼容了华为瀑布屏的左右扩展显示，但是插件默认给了不扩展显示
     * 导致插件用全屏幕分辨率重置播放器大小的时候播放器最下方一部分超出显示区域
     * 导致OSD不显示，此方法对于使用了瀑布屏的华为设备返回真，用于处理显示效果
     */
    private static boolean isHuaweiCurvedDisplay() {

        // 华为曲面屏手机手机高度分辨率有不一样，要单独处理
        if (huaweiCurvedDisplayHackSet == null) {
            huaweiCurvedDisplayHackSet = new HashSet<>();
            huaweiCurvedDisplayHackSet.add("HWLIO"); // mate30 pro/pd
            huaweiCurvedDisplayHackSet.add("HWNOH"); // mate40 pro
            huaweiCurvedDisplayHackSet.add("HWNOP"); // mate40 pd
        }

        String product = Build.DEVICE;

        if (product != null && huaweiCurvedDisplayHackSet.contains(product)) {
            Log.d(TAG, "isHuaweiCurvedDisplay, device = " + product);
            return true;
        } else return false;
    }


    public static Class getContentClass(Collection collection) {
        Class<?> c = Object.class;
        if (collection != null) {
            if (!collection.isEmpty()) {
                c = collection.iterator().next().getClass();
            } else {
                c = Object.class;
            }
        }
        return c;
    }


    public static int getNavigationBarHeight(Context context) {

        Resources resources = context.getResources();

        int resourceId=resources.getIdentifier("navigation_bar_height","dimen","android");

        return 0;
    }


    /**
     * 使用通话模式打开声音有问题，所以判断是该手机，则使用媒体
     * @return
     */
    public static boolean isMeiBlueNote6() {
        return TextUtils.equals("Meizu", getDeviceBrand()) && TextUtils.equals("M6 Note", getSystemModel());
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static float sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}