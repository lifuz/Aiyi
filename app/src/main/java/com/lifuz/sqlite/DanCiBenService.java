package com.lifuz.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lifuz.bean.DanCiBen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 半夏微凉 on 2015/4/29.
 */
public class DanCiBenService {

    private SqlHelper sh;

    public DanCiBenService(Context ctx) {
        this.sh = new SqlHelper(ctx);
    }

    public void insert(String name) {
        SQLiteDatabase db = sh.getWritableDatabase();

        String sql = "insert into danciben(cname) values(?)";

        db.execSQL(sql, new String[]{name});
    }

    public void insertDan(String word, int id) {
        SQLiteDatabase db = sh.getWritableDatabase();
        String sql = "insert into danci(dname,cid) values(?,?)";
        db.execSQL(sql, new Object[]{word, id});
        Log.i("tag", "插入成功");
    }

    public List<String> querryDanci(int id) {
        SQLiteDatabase db = sh.getWritableDatabase();

        List<String> list = new ArrayList<>();

        String sql = "select * from danci where cid = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id+""});
        while (cursor.moveToNext()) {

            Log.i("tag",cursor.getString(cursor.getColumnIndex("dname")));
            list.add(cursor.getString(cursor.getColumnIndex("dname")));
        }
        Log.i("tag",list.toString());
        return list;
    }


    public List<DanCiBen> queryDanciben() {
        SQLiteDatabase db = sh.getWritableDatabase();

        List<DanCiBen> list = new ArrayList<>();

        String sql = "select * from danciben";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {

            DanCiBen dc = new DanCiBen(cursor.getInt(cursor.getColumnIndex("cid")), cursor.getString(cursor.getColumnIndex("cname")));
            list.add(dc);
        }
        return list;
    }

    public boolean queryCheck(int cid, String danci) {

        SQLiteDatabase db = sh.getWritableDatabase();

        String sql = "select * from danci where cid = ? and dname = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{cid + "", danci});
        while (cursor.moveToNext()) {
            return true;

        }


        return false;
    }

    public void deleteDanciben(int id){

        SQLiteDatabase db = sh.getWritableDatabase();
        db.execSQL("delete from danci where cid = ?", new Object[]{id});
        db.execSQL("delete from danciben where cid = ?",new Object[]{id});

    }

    public void updateDanciben(DanCiBen dc){
        SQLiteDatabase db = sh.getWritableDatabase();
        db.execSQL("update danciben set cname = ? where cid = ?" ,new Object[]{dc.getName(),dc.getId()});
    }


}
