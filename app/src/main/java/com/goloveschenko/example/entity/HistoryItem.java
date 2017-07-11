package com.goloveschenko.example.entity;

public class HistoryItem {
    private int id;
    private String date;
    private String expression;
    private String result;
    private String comment;

    public HistoryItem() {
    }

    public HistoryItem(String date, String expression, String comment) {
        this.date = date;
        this.expression = expression;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
