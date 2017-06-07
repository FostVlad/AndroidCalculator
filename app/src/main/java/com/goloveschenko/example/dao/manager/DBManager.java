package com.goloveschenko.example.dao.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.goloveschenko.example.dao.helper.DBHelper;
import com.goloveschenko.example.entity.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static DBManager mInstance;
    private DBHelper dbHelper;

    public static DBManager getInstance(){
        if (mInstance == null) {
            mInstance = new DBManager();
        }
        return mInstance;
    }

    private DBManager() {
    }

    public List<HistoryItem> selectValues(Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<HistoryItem> historyItems = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idColIdx = cursor.getColumnIndex("_id");
            int dateColIdx = cursor.getColumnIndex("date");
            int expressionColIdx = cursor.getColumnIndex("expression");
            int commentColIdx = cursor.getColumnIndex("comment");

            do {
                HistoryItem item = new HistoryItem();
                item.setId(cursor.getInt(idColIdx));
                item.setDate(cursor.getString(dateColIdx));
                item.setExpression(cursor.getString(expressionColIdx));
                item.setComment(cursor.getString(commentColIdx));
                historyItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();
        return historyItems;
    }

    public void insertValue(String expression, String date, String comment, Context context){
        dbHelper = new DBHelper(context);
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.EXPRESSION, expression);
        contentValues.put(DBHelper.DATE, date);
        contentValues.put(DBHelper.COMMENT, comment);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.insert(DBHelper.TABLE_NAME, null, contentValues);

        dbHelper.close();
    }

    public void insertValue(String expression, String date, Context context){
        dbHelper = new DBHelper(context);
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.EXPRESSION, expression);
        contentValues.put(DBHelper.DATE, date);
        contentValues.put(DBHelper.COMMENT, "");

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.insert(DBHelper.TABLE_NAME, null, contentValues);

        dbHelper.close();
    }

    public void updateValue(HistoryItem item, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.EXPRESSION, item.getExpression());
        contentValues.put(DBHelper.DATE, item.getDate());
        contentValues.put(DBHelper.COMMENT, item.getComment());

        String clause = String.format("_id = %s", item.getId());
        database.update(DBHelper.TABLE_NAME, contentValues, clause, null);

        dbHelper.close();
    }

    public void deleteValue(Integer id, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String clause = String.format("_id = %s", id);
        database.delete(DBHelper.TABLE_NAME, clause, null);

        dbHelper.close();
    }

    public void deleteAll(Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_NAME, "", null);

        dbHelper.close();
    }

    public List<HistoryItem> search(String comment, String date, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<HistoryItem> historyItems = new ArrayList<>();

        String clause;
        if (!comment.isEmpty() && !date.isEmpty()) {
            clause = DBHelper.DATE + " = '" + date + "' and " + DBHelper.COMMENT + " LIKE '%" + comment + "%'";
        } else if (!comment.isEmpty() && date.isEmpty()) {
            clause = DBHelper.COMMENT + " LIKE '%" + comment + "%'";
        } else if (comment.isEmpty() && !date.isEmpty()) {
            clause = DBHelper.DATE + " = '" + date + "'";
        } else {
            return historyItems;
        }

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, clause, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColIdx = cursor.getColumnIndex("_id");
            int dateColIdx = cursor.getColumnIndex("date");
            int expressionColIdx = cursor.getColumnIndex("expression");
            int commentColIdx = cursor.getColumnIndex("comment");

            do {
                HistoryItem item = new HistoryItem();
                item.setId(cursor.getInt(idColIdx));
                item.setDate(cursor.getString(dateColIdx));
                item.setExpression(cursor.getString(expressionColIdx));
                item.setComment(cursor.getString(commentColIdx));
                historyItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();
        return historyItems;
    }
}
