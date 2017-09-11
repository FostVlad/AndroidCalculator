package com.goloveschenko.calculator.tools

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.goloveschenko.calculator.R
import com.goloveschenko.calculator.entity.HistoryItem

class HistoryAdapter(private val historyItems: List<HistoryItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class HistoryViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        internal val dateText: TextView = v.findViewById(R.id.history_date) as TextView
        internal val expressionText: TextView = v.findViewById(R.id.history_expression) as TextView
        internal val commentText: TextView = v.findViewById(R.id.history_comment) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val historyItemHolder = holder as HistoryViewHolder
        historyItemHolder.dateText.text = historyItems[pos].date
        historyItemHolder.expressionText.text = historyItems[pos].expression + SYMBOL_EQUALS + historyItems[pos].result
        historyItemHolder.commentText.text = historyItems[pos].comment
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    companion object {
        private val SYMBOL_EQUALS = " = "
    }
}
