package com.example.downloaddemo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 2021.1.4 LHN sp工具类
 */
public class SpUtil {
    public static final String DEFAULT = "DEFAULT_FILE";
    /**
     * 类内部获取sp对象方法
     * @param context 当前上下文
     * @param fileName 文件名
     * @return sp对象
     */
    private static SharedPreferences get(Context context,String fileName){
        return context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
    }

    /**
     * 获取Boolean类型对象
     * @param context
     * @param fileName
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context,String fileName, String key,boolean defValue){
        return get(context,fileName).getBoolean(key,defValue);
    }

    /**
     * 获取int型对象
     * @param context
     * @param fileName
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context,String fileName, String key,int defValue){
        return get(context,fileName).getInt(key,defValue);
    }

    /**
     * 获取int型对象
     * @param context
     * @param fileName
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context,String fileName, String key,String defValue){
        return get(context,fileName).getString(key,defValue);
    }

    /**
     * 放入键值
     * @param context 上下文环境
     * @param key 键
     * @param value 值
     */
    public static void put(Context context,String key,Object value){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = get(context,DEFAULT).edit();
        if (value instanceof Integer){
            editor.putInt(key,(int)value);
        }else if (value instanceof String){
            editor.putString(key,(String) value);
        }else if (value instanceof Boolean){
            editor.putBoolean(key,(boolean)value);
        }else {
            Log.e("SP_ERROR","TYPE ERROR");
            return;
        }
        editor.apply();
    }

    public static String toString(Context context){
        return get(context,DEFAULT).getAll().toString();
    }

    public static void remove(Context context,String key){
        get(context,DEFAULT).edit().remove(key).apply();
    }
}
