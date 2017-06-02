package com.goloveschenko.example.dao.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.goloveschenko.example.dao.helper.DBHelper;

public class DBManager {
    private static DBManager mInstance;
    private DBHelper dbHelper;

    public static DBManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new DBManager(context);
        }
        return mInstance;
    }

    private DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public Cursor selectValues(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
    }

    public void insertValue(String expression, String date, String comment){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.EXPRESSION, expression);
        contentValues.put(DBHelper.DATE, date);
        contentValues.put(DBHelper.COMMENT, comment);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.insert(DBHelper.TABLE_NAME, null, contentValues);
    }

    public void insertValue(String expression, String date){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.EXPRESSION, expression);
        contentValues.put(DBHelper.DATE, date);
        contentValues.put(DBHelper.COMMENT, "");

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.insert(DBHelper.TABLE_NAME, null, contentValues);
    }

    public void deleteValue(Integer id){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String clause = String.format("WHERE ID = %s", id);
        database.delete(DBHelper.TABLE_NAME, clause, null);
    }
}
