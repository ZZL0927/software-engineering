package com.cc.anniversary.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DAO {

    private final SQLiteDatabase writableDatabase;
    public static final String table = "anniversary";

    public DAO(Context context) {
        writableDatabase = new DBHelper(context).getWritableDatabase();
    }

    //插入日程
    public long insert(CalendarEntity calendarEntity) throws SQLException {
        ContentValues value = new ContentValues();
        value.put(CalendarEntity.NAME, calendarEntity.getName());
        value.put(CalendarEntity.DESCRIPTION, calendarEntity.getDescription());
        value.put(CalendarEntity.TIME, calendarEntity.getTime());
        return
                writableDatabase.insertOrThrow(table, null, value);
    }

    //通过日期查询日程
    public CalendarEntity getInfoByData(String s) {
        CalendarEntity calendarEntity = null;
        Cursor cursor = writableDatabase.rawQuery("select * from " + table + " where time=?", new String[]{s});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            calendarEntity = new CalendarEntity();
            calendarEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
            calendarEntity.setDescription(cursor.getString(cursor.getColumnIndex(CalendarEntity.DESCRIPTION)));
            calendarEntity.setTime(cursor.getString(cursor.getColumnIndex(CalendarEntity.TIME)));
            calendarEntity.setId(cursor.getString(cursor.getColumnIndex("id")));
            cursor.close();
        }
        return calendarEntity;
    }

    //查询全部日程
    public List<CalendarEntity> getInfoList() {
        List<CalendarEntity> entityList = new ArrayList<>();
        Cursor cursor = writableDatabase.rawQuery("select * from " + table, null);
        while (cursor.moveToNext()) {
            CalendarEntity calendarEntity = calendarEntity = new CalendarEntity();
            calendarEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
            calendarEntity.setDescription(cursor.getString(cursor.getColumnIndex(CalendarEntity.DESCRIPTION)));
            calendarEntity.setTime(cursor.getString(cursor.getColumnIndex(CalendarEntity.TIME)));
            calendarEntity.setId(cursor.getString(cursor.getColumnIndex("id")));
            entityList.add(calendarEntity);
        }
        cursor.close();
        return entityList;
    }

    public long update(CalendarEntity calendarEntity) {
        ContentValues value = new ContentValues();
        value.put(CalendarEntity.NAME, calendarEntity.getName());
        value.put(CalendarEntity.DESCRIPTION, calendarEntity.getDescription());
        value.put(CalendarEntity.TIME, calendarEntity.getTime());
        return
                writableDatabase.updateWithOnConflict(table, value, "id = ?", new String[]{calendarEntity.getId()}, 5);
    }
}
