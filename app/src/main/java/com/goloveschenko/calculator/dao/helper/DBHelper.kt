package com.goloveschenko.calculator.dao.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                ID + " integer primary key autoincrement, " +
                DATE + ", " +
                EXPRESSION + ", " +
                RESULT + ", " +
                COMMENT + ")")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "calculatorDb"

        val TABLE_NAME = "calculator"

        val ID = "id"
        val EXPRESSION = "expression"
        val RESULT = "result"
        val DATE = "date"
        val COMMENT = "comment"
    }
}
