package com.goloveschenko.calculator.dao.manager

import android.database.Cursor
import android.database.CursorWrapper

import com.goloveschenko.calculator.dao.helper.DBHelper
import com.goloveschenko.calculator.entity.HistoryItem

class HistoryCursor(cursor:Cursor):CursorWrapper(cursor) {
    fun getHistoryItem(): HistoryItem {
        val id = getLong(getColumnIndex(DBHelper.ID))
        val date = getString(getColumnIndex(DBHelper.DATE))
        val expression = getString(getColumnIndex(DBHelper.EXPRESSION))
        val result = getString(getColumnIndex(DBHelper.RESULT))
        val comment = getString(getColumnIndex(DBHelper.COMMENT))

        return HistoryItem(id,
                        date,
                        expression,
                        result,
                        comment)
    }
}
