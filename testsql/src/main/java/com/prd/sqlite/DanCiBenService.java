package com.prd.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by 半夏微凉 on 2015/4/29.
 */
public class DanCiBenService {

    private SqlHelper sh;

    public DanCiBenService(Context ctx){
        this.sh = new SqlHelper(ctx);
    }

    public void  insert() {
        SQLiteDatabase db = sh.getWritableDatabase();

        String sql = "insert into danciben(cname) values(?)";

        db.execSQL(sql,new String[] {"lifu"});
    }

    public void insertDan(){
        SQLiteDatabase db = sh.getWritableDatabase();
        String sql = "insert into danci(dname,cid) values(?,?)";
        db.execSQL(sql,new Object[] {"lifuz",1});
        Log.i("tag","插入成功");
    }

    public String queryAll() {
        SQLiteDatabase db = sh.getWritableDatabase();

        String sql = "select * from danciben c left join danci d on c.cid = d.cid";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        while (cursor.moveToNext()) {
            String str = cursor.getString(cursor.getColumnIndex("cname"));
            str = str + cursor.getString(cursor.getColumnIndex("dname"));
            return str;
        }

        return null;
    }

    public String query() {
        SQLiteDatabase db = sh.getWritableDatabase();
        String sql = "select * from danciben";
        Cursor cursor = db.rawQuery(sql,new String[]{});
        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex("cname"));
        }
        return  null;
    }


}
