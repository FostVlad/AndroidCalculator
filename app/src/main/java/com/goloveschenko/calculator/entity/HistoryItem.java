package com.goloveschenko.calculator.entity;

import com.goloveschenko.calculator.activity.HistoryActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryItem {
    private long id;
    private String date;
    private String expression;
    private String result;
    private String comment;

    public HistoryItem() {
        SimpleDateFormat sdf = new SimpleDateFormat(HistoryActivity.DATE_FORMAT, Locale.US);
        this.date = sdf.format(new Date());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
