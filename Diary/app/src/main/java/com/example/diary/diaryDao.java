package com.example.diary;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class diaryDao {
    private databaseHelper dbhelper;// 创建databaseHelper对象
    private SQLiteDatabase db;// 创建SQLiteDatabase对象

    public diaryDao(Context context)// 定义构造函数
    {
        dbhelper = new databaseHelper(context);// 初始化databaseHelper对象
    }

    public void insert(diary diary){
        String s="insert into diary (title,content,date,author,image) values ('"+diary.getTitle()+"','"+
                diary.getContent()+"','"+diary.getDate()+"','"+diary.getAuthor()+"','"+diary.getImage()+"')";
        db.execSQL(s);
    }

    @SuppressLint("Range")
    public diary find(String title) {
        db = dbhelper.getWritableDatabase();
        String sql = "select * from diary where title=?";
        String[] selectionArgs = new String[] { title };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            diary d = new diary();
            d.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            d.setContent(cursor.getString(cursor.getColumnIndex("content")));
            d.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            d.setDate(cursor.getString(cursor.getColumnIndex("date")));
            d.setImage(cursor.getString(cursor.getColumnIndex("image")));
            return d;
        }
        return null;// 没有返回null
    }

    public void update(diary d) {
        db = dbhelper.getWritableDatabase();
        String sql = "update diary set title=?,content=?,image=? where date=? and author=?";
        Object bindArgs[] = new Object[] { d.getTitle(), d.getContent(),d.getImage(),d.getDate(),d.getAuthor() };
        db.execSQL(sql, bindArgs);
    }

    @SuppressLint("Range")
    public ArrayList<diary> findall(String a) {
        ArrayList<diary> diarylist = new ArrayList<diary>();
        db = dbhelper.getWritableDatabase();
        String sql = "select * from diary where author=?";
        String[] selectionArgs = new String[] { a };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        // 游标从头读到尾
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                diary d = new diary();
               d.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
               d.setContent(cursor.getString(cursor.getColumnIndex("content")));
               d.setTitle(cursor.getString(cursor.getColumnIndex("title")));
               d.setDate(cursor.getString(cursor.getColumnIndex("date")));
               d.setImage(cursor.getString(cursor.getColumnIndex("image")));
                diarylist.add(d);
        }
        return diarylist;
    }

    public void delete(diary d) {
        db = dbhelper.getWritableDatabase();
        String sql = "delete from diary where author=? and date=?";
        Object bindArgs[] = new Object[] { d.getAuthor(),d.getDate()};
        db.execSQL(sql, bindArgs);
    }
    public void close(){
        db = dbhelper.getWritableDatabase();
        if(db != null){
            db.close();
        }
    }
}
