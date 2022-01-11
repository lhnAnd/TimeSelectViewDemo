package com.example.downloaddemo.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;

/**
 * 2022.1.11 lhn
 * 文件路径管理工具
 */
public class FileUtil {
    private static final String DEFAULT_FILE_PATH = "DownloadDemo";
    private static final String TAG = FileUtil.class.getSimpleName();

    /**
     * 获取外部存储是否可用
     * @return 返回结果
     */
    private static boolean hasExternalStorageAva(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取缓存文件夹路径,注意这里只是获取了路径并没有创建文件夹。
     * @param context
     * @return
     */
    public static File getGenericFilesDir(Context context){
        if (hasExternalStorageAva()){
            Log.d(TAG,"external storage is available.");
            File dir = context.getExternalFilesDir(null);
            if(dir == null){
                return new File(context.getFilesDir(),DEFAULT_FILE_PATH);
            }else {
                Log.d(TAG,"dir is not null.");
                return new File(dir,DEFAULT_FILE_PATH);
            }
        }else {
            File dir = context.getFilesDir();
            return new File(dir,DEFAULT_FILE_PATH);
        }
    }
}
