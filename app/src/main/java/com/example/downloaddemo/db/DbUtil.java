package com.example.downloaddemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

/**
 * 2021.1.11 lhn
 * SQLite 数据库工具
 */
public class DbUtil {
    private static final String TAG = "DbUtil";
    private SQLiteDatabase database;
    private static final String Auto_LOGIN = "autoLogin";

    /**
     * 内部私有构造函数
     * @param context
     * @param dbName
     */
    private DbUtil(Context context,String dbName){
        this.database = context.openOrCreateDatabase(dbName,0,null);
    }

    public static DbUtil getInstance(Context context){
        return new DbUtil(context,Auto_LOGIN);
    }

    public void createTable(){
        String stu_table2="create table usertable(_id integer primary key autoincrement,sname text,snumber text)";
        database.execSQL(stu_table2);
    }

    public void insert(){
        ContentValues cValue = new ContentValues();
        cValue.put("sname","xiao");
        cValue.put("snumber","0002");
        database.insert("usertable",null,cValue);
    }

    public void query(){
        Cursor cursor = database.query("usertable",null,null,null,null,null,null);

//判断游标是否为空
        if(cursor.moveToFirst()){
//遍历游
            Log.e(TAG,""+cursor.getCount());
            for(int i=0;i<cursor.getCount()-1;i++){
                cursor.moveToFirst();
                cursor.move(i);
//获得ID
                int id = cursor.getInt(0);
//获得用户名
                String username=cursor.getString(1);
//获得密码
                String password=cursor.getString(2);
//输出用户信息
                System.out.println(id+":"+id+":"+username+":"+password);
            }
        }
    }
}
