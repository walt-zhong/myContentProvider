package com.walt.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * description:
 * Author:zhongxj
 * Email:xianjian@nolovr.com
 * Date:2023/1/1
 */
public class EmployeeProvider extends ContentProvider {
    private DBHelper dbHelper;
    private static final UriMatcher sUriMatcher;
    public static String callFuncRes = "";
    //查询更新条件
    private static final int EMPLOYEE = 1;
    private static final int EMPLOYEE_ID = 2;

    //查询集合
    private static final HashMap<String, String> empProjectionMap;

    static {
        //Uri匹配工具类
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Employee.AUTHORITY, "employee", EMPLOYEE);
        sUriMatcher.addURI(Employee.AUTHORITY, "employee/#", EMPLOYEE_ID);

        empProjectionMap = new HashMap<>();
        empProjectionMap.put(Employee._ID, Employee._ID);
        empProjectionMap.put(Employee.NAME, Employee.NAME);
        empProjectionMap.put(Employee.GENDER, Employee.GENDER);
        empProjectionMap.put(Employee.AGE, Employee.AGE);
    }

    @Override
    public boolean onCreate() {
        //实例化数据库帮助类
        dbHelper = new DBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Log.d("zhongxj", "method: " + method);
        if ("func".equals(method)) {
            int actionCode = extras.getInt("actionCode");
            String extraData = extras.getString("extraData");
            StringBuilder sb = new StringBuilder();
            sb.append("func arg: ").append(arg)
                    .append("actionCode: ").append(actionCode)
                    .append("extraData").append(extraData)
                    .append("methodName: ").append(method);
            callFuncRes = sb.toString();
            Log.d("zhongxj", "result: " + sb.toString());

        }

        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case EMPLOYEE:
                qb.setTables(DBHelper.EMPLOYEES_TABLE_NAME);
                qb.setProjectionMap(empProjectionMap);
                break;

            case EMPLOYEE_ID:
                qb.setTables(DBHelper.EMPLOYEES_TABLE_NAME);
                qb.setProjectionMap(empProjectionMap);
                qb.appendWhere(Employee._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("URI error: " + uri);

        }

        //查出的数据使用默认排序
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = Employee.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //获取数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //插入数据,返回行ID
        long rowId = db.insert(DBHelper.EMPLOYEES_TABLE_NAME, Employee.NAME, values);
        if (rowId > 0) {
            Uri empUri = ContentUris.withAppendedId(Employee.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(empUri, null);
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case EMPLOYEE:
                count = db.delete(DBHelper.EMPLOYEES_TABLE_NAME, selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.delete(DBHelper.EMPLOYEES_TABLE_NAME, Employee._ID + "=" + noteId
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("URI error: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case EMPLOYEE:
                count = db.update(DBHelper.EMPLOYEES_TABLE_NAME, values, selection, selectionArgs);
                break;

            case EMPLOYEE_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.update(DBHelper.EMPLOYEES_TABLE_NAME, values, Employee._ID + "=" + noteId
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("URI error: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
