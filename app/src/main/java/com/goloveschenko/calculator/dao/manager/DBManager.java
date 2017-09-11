package com.goloveschenko.calculator.dao.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.goloveschenko.calculator.dao.helper.DBHelper;
import com.goloveschenko.calculator.entity.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static DBManager mInstance;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public static DBManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new DBManager(context.getApplicationContext());
        }
        return mInstance;
    }

    private DBManager() {
    }

    private DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    private HistoryCursor queryItems(String whereClause, String[] whereArgs){
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        return new HistoryCursor(cursor);
    }

    private ContentValues getContentValues(HistoryItem item){
        ContentValues values = new ContentValues();
        values.put(DBHelper.EXPRESSION, item.getExpression());
        values.put(DBHelper.RESULT, item.getResult());
        values.put(DBHelper.DATE, item.getDate());
        values.put(DBHelper.COMMENT, item.getComment());

        return values;
    }

    public List<HistoryItem> selectValues(){
        database = dbHelper.getReadableDatabase();

        List<HistoryItem> historyItems = new ArrayList<>();
        HistoryCursor cursor = queryItems(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                historyItems.add(cursor.getHistoryItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        database.close();
        return historyItems;
    }

    public void insertValue(HistoryItem item){
        ContentValues values = getContentValues(item);
        database = dbHelper.getWritableDatabase();
        database.insert(DBHelper.TABLE_NAME, null, values);

        database.close();
    }

    public void updateValue(HistoryItem item){
        database = dbHelper.getWritableDatabase();
        ContentValues values = getContentValues(item);
        String id = String.valueOf(item.getId());
        database.update(DBHelper.TABLE_NAME, values, DBHelper.ID + " = ?", new String[] {id});

        database.close();
    }

    public void deleteValue(long id){
        database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_NAME, DBHelper.ID + " = ?", new String[] {String.valueOf(id)});

        database.close();
    }

    public void deleteAll(){
        database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_NAME, "", null);

        database.close();
    }

    public List<HistoryItem> search(String date, String comment){
        database = dbHelper.getReadableDatabase();
        List<HistoryItem> historyItems = new ArrayList<>();

        HistoryCursor cursor;
        if (!comment.isEmpty() && !date.isEmpty()) {
            cursor = queryItems(DBHelper.DATE + " = '?' and " + DBHelper.COMMENT + " LIKE '%?%'", new String[] {date, comment});
        } else if (!comment.isEmpty() && date.isEmpty()) {
            cursor = queryItems(DBHelper.COMMENT + " LIKE '%?%'", new String[] {comment});
        } else if (comment.isEmpty() && !date.isEmpty()) {
            cursor = queryItems(DBHelper.DATE + " = '?'", new String[] {date});
        } else {
            return historyItems;
        }

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                historyItems.add(cursor.getHistoryItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        database.close();
        return historyItems;
    }
}
