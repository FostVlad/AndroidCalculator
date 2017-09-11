package com.goloveschenko.calculator.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.goloveschenko.calculator.R
import com.goloveschenko.calculator.dao.manager.DBManager
import com.goloveschenko.calculator.entity.HistoryItem
import com.goloveschenko.calculator.tools.HistoryAdapter
import com.goloveschenko.calculator.tools.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_history.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {
    private var historyItems: MutableList<HistoryItem> = ArrayList()
    private var adapter: HistoryAdapter = HistoryAdapter(historyItems)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                searchItems()
                return true
            }
            R.id.menu_clear -> {
                clearItems()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyView.layoutManager = LinearLayoutManager(this)

        historyItems = DBManager.getInstance(historyView.context).selectValues()
        adapter = HistoryAdapter(historyItems)
        historyView.adapter = adapter

        historyView.addOnItemTouchListener(RecyclerItemClickListener(historyView.context, historyView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val res = resources
                val menuItems = res.getStringArray(R.array.history_item_input_menu)
                val item = historyItems[position]
                val menuBuilder = AlertDialog.Builder(view.context)
                menuBuilder.setTitle(R.string.history_input_title)
                menuBuilder.setItems(menuItems) { dialog, which ->
                    var result = ""
                    when (which) {
                        MENU_INPUT_RESULT -> result = item.result
                        MENU_INPUT_EXPRESSION -> result = item.expression
                    }
                    val intent = Intent()
                    intent.putExtra(EXTRA_EXPRESSION_RESULT, result)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                val alert = menuBuilder.create()
                alert.show()
            }

            override fun onLongItemClick(view: View, position: Int) {
                val res = resources
                val menuItems = res.getStringArray(R.array.history_item_menu)
                val menuBuilder = AlertDialog.Builder(view.context)
                menuBuilder.setTitle(R.string.history_item_alert_title)
                menuBuilder.setItems(menuItems) { dialog, which ->
                    when (which) {
                        MENU_COMMENT -> {
                            val commentBuilder = AlertDialog.Builder(historyView.context)
                            val input = EditText(historyView.context)
                            input.inputType = InputType.TYPE_CLASS_TEXT
                            commentBuilder
                                    .setCancelable(false)
                                    .setTitle(R.string.input_comment_title)
                                    .setView(input)
                                    .setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }
                                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                                        val item = historyItems[position]
                                        val comment = input.text.toString()
                                        item.comment = comment
                                        DBManager.getInstance(historyView.context).updateValue(item)
                                        adapter.notifyItemChanged(position)
                                    }
                            val alertComment = commentBuilder.create()
                            alertComment.show()
                            val comment = historyItems[position].comment
                            if (comment != null) {
                                input.setText(comment)
                                input.setSelection(0, comment.length)
                            }
                        }
                        MENU_DELETE -> {
                            val deleteBuilder = AlertDialog.Builder(historyView.context)
                            deleteBuilder
                                    .setMessage(R.string.confirm_delete_title)
                                    .setCancelable(false)
                                    .setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }
                                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                                        val item = historyItems[position]
                                        DBManager.getInstance(historyView.context).deleteValue(item.id!!)
                                        historyItems.removeAt(position)
                                        adapter.notifyItemRemoved(position)
                                    }
                            val alertDelete = deleteBuilder.create()
                            alertDelete.show()
                        }
                    }
                }
                val alert = menuBuilder.create()
                alert.show()
            }
        }))
    }

    fun searchItems() {
        val searchBuilder = AlertDialog.Builder(historyView.context)
        val view = layoutInflater.inflate(R.layout.search_panel, null)
        searchBuilder
                .setMessage(R.string.search_title)
                .setCancelable(false)
                .setView(view)
                .setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    val commentText = view.findViewById(R.id.search_comment) as EditText
                    val comment = commentText.text.toString()
                    val dateText = view.findViewById(R.id.search_date) as EditText
                    val date = dateText.text.toString()
                    historyItems = DBManager.getInstance(historyView.context).search(comment, date)
                    adapter = HistoryAdapter(historyItems)
                    historyView.adapter = adapter
                }
        val alert = searchBuilder.create()
        alert.show()
    }

    fun clearItems() {
        val clearBuilder = AlertDialog.Builder(historyView.context)
        clearBuilder
                .setMessage(R.string.confirm_clear_title)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel) { dialog, which -> dialog.cancel() }
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    DBManager.getInstance(historyView.context).deleteAll()
                    historyItems.clear()
                    adapter.notifyDataSetChanged()
                }
        val alert = clearBuilder.create()
        alert.show()
    }

    fun OnDateTextClick(view: View) {
        val dateText = view.findViewById(R.id.search_date) as EditText
        val calendar = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
            val date = sdf.format(calendar.time)
            dateText.setText(date)
        }

        val picker = DatePickerDialog(view.context, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        picker.show()
    }

    companion object {
        val DATE_FORMAT = "dd MMM yyyy"
        val EXTRA_EXPRESSION_RESULT = "result"

        val MENU_INPUT_RESULT = 0
        val MENU_INPUT_EXPRESSION = 1
        val MENU_COMMENT = 0
        val MENU_DELETE = 1
    }
}
