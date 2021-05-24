package com.cc.anniversary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "anniversary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建纪念日表（ID，name，description，类型，备注）
        db.execSQL("create table " + DAO.table + "(id integer primary key," +
                CalendarEntity.NAME + " text," +
                CalendarEntity.DESCRIPTION + " text," +
                CalendarEntity.TIME + " text  NOT NULL UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
