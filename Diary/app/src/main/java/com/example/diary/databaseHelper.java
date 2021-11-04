package com.example.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 创建数据库
 */
public class databaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_DIARY = "create table diary ("+
            "id integer primary key autoincrement,title text,content text"+
            ",date Date,author text,image text)";
    private Context context;
    public databaseHelper(@Nullable Context context) {
        super(context, "diary.db", null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
