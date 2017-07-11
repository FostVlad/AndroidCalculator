package com.goloveschenko.example.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "calculatorDb";

    public static final String TABLE_NAME = "calculator";

    public static final String ID = "id";
    public static final String EXPRESSION = "expression";
    public static final String RESULT = "result";
    public static final String DATE = "date";
    public static final String COMMENT = "comment";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                ID + " integer primary key autoincrement, " +
                DATE + ", " +
                EXPRESSION + ", " +
                RESULT + ", " +
                COMMENT + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
