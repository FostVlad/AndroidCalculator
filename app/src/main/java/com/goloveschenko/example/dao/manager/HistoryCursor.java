package com.goloveschenko.example.dao.manager;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.goloveschenko.example.dao.helper.DBHelper;
import com.goloveschenko.example.entity.HistoryItem;

public class HistoryCursor extends CursorWrapper {
    public HistoryCursor(Cursor cursor) {
        super(cursor);
    }

    public HistoryItem getHistoryItem() {
        long id = getLong(getColumnIndex(DBHelper.ID));
        String expression = getString(getColumnIndex(DBHelper.EXPRESSION));
        String result = getString(getColumnIndex(DBHelper.RESULT));
        String date = getString(getColumnIndex(DBHelper.DATE));
        String comment = getString(getColumnIndex(DBHelper.COMMENT));

        HistoryItem item = new HistoryItem();
        item.setId(id);
        item.setExpression(expression);
        item.setResult(result);
        item.setDate(date);
        item.setComment(comment);
        return item;
    }
}
