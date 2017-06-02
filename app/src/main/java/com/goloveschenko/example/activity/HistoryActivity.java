package com.goloveschenko.example.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.goloveschenko.example.R;
import com.goloveschenko.example.dao.HistoryAdapter;
import com.goloveschenko.example.dao.manager.DBManager;
import com.goloveschenko.example.entity.HistoryItem;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyView;
    private RecyclerView.Adapter adapter;

    private void installHistoryView(){
        Cursor cursor = DBManager.getInstance(getApplicationContext()).selectValues();
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

        adapter = new HistoryAdapter(historyItems);
        historyView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyView = (RecyclerView) findViewById(R.id.historyView);
        historyView.setLayoutManager(new LinearLayoutManager(this));

        installHistoryView();
    }
}
