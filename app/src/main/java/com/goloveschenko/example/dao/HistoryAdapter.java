package com.goloveschenko.example.dao;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goloveschenko.example.R;
import com.goloveschenko.example.entity.HistoryItem;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private ArrayList<HistoryItem> historyItems;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView expressionTextView;
        public TextView commentTextView;

        public HistoryViewHolder(View v) {
            super(v);
            dateTextView = (TextView) v.findViewById(R.id.history_date);
            expressionTextView = (TextView) v.findViewById(R.id.history_expression);
            commentTextView = (TextView) v.findViewById(R.id.history_comment);
        }
    }

    public HistoryAdapter(ArrayList<HistoryItem> historyItems) {
        this.historyItems = historyItems;
    }

    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        HistoryViewHolder vh = new HistoryViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int pos) {
        holder.dateTextView.setText(historyItems.get(pos).getDate());
        holder.expressionTextView.setText(historyItems.get(pos).getExpression());
        holder.commentTextView.setText(historyItems.get(pos).getComment());
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
