package com.walt.mycontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * description:
 * Author:zhongxj
 * Email:xianjian@nolovr.com
 * Date:2023/1/1
 */
public class DBHelper extends SQLiteOpenHelper {
    //数据库名称
    private static final String DATABASE_NAME = "employees.db";

    //数据库版本
    private static final int DATABASE_VERSION = 1;

    //表名
    public static final String EMPLOYEES_TABLE_NAME = "employee";
    public DBHelper(@Nullable Context context) {
        //创建数据库
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EMPLOYEES_TABLE_NAME + "(" +
                Employee._ID + " INTEGER PRIMARY KEY,"
                +Employee.NAME + " TEXT,"
                +Employee.GENDER + " TEXT,"
                +Employee.AGE + " INTEGER"
                + ");");
    }

    //版本更新时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //版本更新时删除表重新创建
        db.execSQL("DROP TABLE IF EXISTS " + EMPLOYEES_TABLE_NAME);
        onCreate(db);
    }
}
