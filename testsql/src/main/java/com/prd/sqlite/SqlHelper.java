package com.prd.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 半夏微凉 on 2015/4/29.
 */
public class SqlHelper extends SQLiteOpenHelper {

    public SqlHelper(Context ctx) {
        super(ctx, "cidian.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table danciben(cid integer primary key autoincrement,cname varchar(20))";
        db.execSQL(sql);

        Log.i("tag", "创建成功");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "create table danci(did integer primary key autoincrement," +
                "dname varchar(20),cid integer," +
                " FOREIGN KEY(cid) REFERENCES danciben(cid) )";
        db.execSQL(sql);
        Log.i("tag","更新成功");
    }
}
