package com.goloveschenko.calculator.dao.manager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.goloveschenko.calculator.dao.helper.DBHelper
import com.goloveschenko.calculator.entity.HistoryItem
import java.util.*

class DBManager private constructor(context: Context){
    private val dbHelper: DBHelper = DBHelper(context)
    private var database: SQLiteDatabase = dbHelper.writableDatabase

    private fun queryItems(whereClause: String?, whereArgs: Array<String>?): HistoryCursor {
        database = dbHelper.readableDatabase
        val cursor = database.query(DBHelper.TABLE_NAME, null, whereClause, whereArgs, null, null, null)
        return HistoryCursor(cursor)
    }

    private fun getContentValues(item: HistoryItem): ContentValues {
        val values = ContentValues()
        values.put(DBHelper.EXPRESSION, item.expression)
        values.put(DBHelper.RESULT, item.result)
        values.put(DBHelper.DATE, item.date)
        values.put(DBHelper.COMMENT, item.comment)

        return values
    }

    fun selectValues(): MutableList<HistoryItem> {
        database = dbHelper.readableDatabase

        val historyItems = ArrayList<HistoryItem>()
        val cursor = queryItems(null, null)

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                historyItems.add(cursor.getHistoryItem())
                cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }

        database.close()
        return historyItems
    }

    fun insertValue(item: HistoryItem) {
        val values = getContentValues(item)
        database = dbHelper.writableDatabase
        database.insert(DBHelper.TABLE_NAME, null, values)

        database.close()
    }

    fun updateValue(item: HistoryItem) {
        database = dbHelper.writableDatabase
        val values = getContentValues(item)
        val id = item.id.toString()
        database.update(DBHelper.TABLE_NAME, values, DBHelper.ID + " = ?", arrayOf(id))

        database.close()
    }

    fun deleteValue(id: Long) {
        database = dbHelper.writableDatabase
        database.delete(DBHelper.TABLE_NAME, DBHelper.ID + " = ?", arrayOf(id.toString()))

        database.close()
    }

    fun deleteAll() {
        database = dbHelper.writableDatabase
        database.delete(DBHelper.TABLE_NAME, "", null)

        database.close()
    }

    fun search(date: String, comment: String): MutableList<HistoryItem> {
        database = dbHelper.readableDatabase
        val historyItems = ArrayList<HistoryItem>()

        val cursor: HistoryCursor
        if (!comment.isEmpty() && !date.isEmpty()) {
            cursor = queryItems(DBHelper.DATE + " = '?' and " + DBHelper.COMMENT + " LIKE '%?%'", arrayOf(date, comment))
        } else if (!comment.isEmpty() && date.isEmpty()) {
            cursor = queryItems(DBHelper.COMMENT + " LIKE '%?%'", arrayOf(comment))
        } else if (comment.isEmpty() && !date.isEmpty()) {
            cursor = queryItems(DBHelper.DATE + " = '?'", arrayOf(date))
        } else {
            return historyItems
        }

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                historyItems.add(cursor.getHistoryItem())
                cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }

        database.close()
        return historyItems
    }

    companion object {
        private var mInstance: DBManager? = null

        fun getInstance(context: Context): DBManager {
            if (mInstance == null) {
                mInstance = DBManager(context.applicationContext)
            }
            return mInstance!!
        }
    }
}
