package com.walt.mycontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tv_result);
    }

    private void setText(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResult.setText(text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText(EmployeeProvider.callFuncRes);
    }

    public void insert(View view) {
        Uri uri = Employee.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Employee.NAME,"walt");
        values.put(Employee.GENDER,"male");
        values.put(Employee.AGE,28);
        getContentResolver().insert(uri,values);
        Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
        setText("插入成功 =>" + values.toString());
    }

    public void delete(View view) {
        //删除ID为1的记录
        Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI,2);
        int delete = getContentResolver().delete(uri, null, null);
        setText("删除成功 result: " + delete);
    }

    public void update(View view) {
        //更新ID为1的记录
        Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI,1);
        ContentValues values = new ContentValues();
        values.put(Employee.NAME,"walt-zhong");
        values.put(Employee.GENDER,"male");
        values.put(Employee.AGE,18);
        int update = getContentResolver().update(uri, values, null, null);
        setText("更新成功 result: " + update);
    }

    public void query(View view) {
        String [] PROJECTION = new String[]{
                Employee._ID,
                Employee.NAME,
                Employee.GENDER,
                Employee.AGE
        };

        Cursor cursor = getContentResolver().query(Employee.CONTENT_URI,PROJECTION,null,null,Employee.DEFAULT_SORT_ORDER);

        StringBuilder sb = new StringBuilder();

        if(cursor !=null && cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String gender = cursor.getString(2);
                int age = cursor.getInt(3);
                sb.append("id").append(id).append("name: ").append(name).append(" ,gender: ")
                        .append(gender).append(" ,age: ").append(age).append("\n");
            }
        }

        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        setText("");
        setText(sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}