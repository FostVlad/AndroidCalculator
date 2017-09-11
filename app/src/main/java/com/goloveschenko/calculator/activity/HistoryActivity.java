package com.goloveschenko.calculator.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.goloveschenko.calculator.tools.HistoryAdapter;
import com.goloveschenko.calculator.R;
import com.goloveschenko.calculator.dao.manager.DBManager;
import com.goloveschenko.calculator.entity.HistoryItem;
import com.goloveschenko.calculator.tools.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    public static final String DATE_FORMAT = "dd MMM yyyy";
    public static final String EXTRA_EXPRESSION_RESULT = "result";

    public static final int MENU_INPUT_RESULT = 0;
    public static final int MENU_INPUT_EXPRESSION = 1;
    public static final int MENU_COMMENT = 0;
    public static final int MENU_DELETE = 1;

    private RecyclerView historyView;
    private RecyclerView.Adapter adapter;
    private List<HistoryItem> historyItems;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                searchItems();
                return true;
            case R.id.menu_clear:
                clearItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyView = (RecyclerView) findViewById(R.id.historyView);
        historyView.setLayoutManager(new LinearLayoutManager(this));

        historyItems = DBManager.getInstance(historyView.getContext()).selectValues();
        adapter = new HistoryAdapter(historyItems);
        historyView.setAdapter(adapter);

        historyView.addOnItemTouchListener(new RecyclerItemClickListener(historyView.getContext(), historyView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Resources res = getResources();
                String[] menuItems = res.getStringArray(R.array.history_item_input_menu);
                final HistoryItem item = historyItems.get(position);
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(view.getContext());
                menuBuilder.setTitle(R.string.history_input_title);
                menuBuilder.setItems(menuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = "";
                        switch (which) {
                            case MENU_INPUT_RESULT:
                                result = item.getResult();
                                break;
                            case MENU_INPUT_EXPRESSION:
                                result = item.getExpression();
                                break;
                        }
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_EXPRESSION_RESULT, result);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                AlertDialog alert = menuBuilder.create();
                alert.show();
            }

            @Override
            public void onLongItemClick(View view, final int position) {
                Resources res = getResources();
                String[] menuItems = res.getStringArray(R.array.history_item_menu);
                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(view.getContext());
                menuBuilder.setTitle(R.string.history_item_alert_title);
                menuBuilder.setItems(menuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case MENU_COMMENT:
                                AlertDialog.Builder commentBuilder = new AlertDialog.Builder(historyView.getContext());
                                final EditText input = new EditText(historyView.getContext());
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                commentBuilder
                                        .setCancelable(false)
                                        .setTitle(R.string.input_comment_title)
                                        .setView(input)
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                HistoryItem item = historyItems.get(position);
                                                String comment = input.getText().toString();
                                                if (comment.isEmpty()) {
                                                    comment = null;
                                                }
                                                item.setComment(comment);
                                                DBManager.getInstance(historyView.getContext()).updateValue(item);
                                                adapter.notifyItemChanged(position);
                                            }
                                        });
                                AlertDialog alertComment = commentBuilder.create();
                                alertComment.show();
                                String comment = historyItems.get(position).getComment();
                                if (comment != null) {
                                    input.setText(comment);
                                    input.setSelection(0, comment.length());
                                }
                                break;
                            case MENU_DELETE:
                                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(historyView.getContext());
                                deleteBuilder
                                        .setMessage(R.string.confirm_delete_title)
                                        .setCancelable(false)
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                HistoryItem item = historyItems.get(position);
                                                DBManager.getInstance(historyView.getContext()).deleteValue(item.getId());
                                                historyItems.remove(position);
                                                adapter.notifyItemRemoved(position);
                                            }
                                        });
                                AlertDialog alertDelete = deleteBuilder.create();
                                alertDelete.show();
                                break;
                        }
                    }
                });
                AlertDialog alert = menuBuilder.create();
                alert.show();
            }
        }));
    }

    public void searchItems() {
        AlertDialog.Builder searchBuilder = new AlertDialog.Builder(historyView.getContext());
        final View view = getLayoutInflater().inflate(R.layout.search_panel, null);
        searchBuilder
                .setMessage(R.string.search_title)
                .setCancelable(false)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText commentText = (EditText) view.findViewById(R.id.search_comment);
                        String comment = commentText.getText().toString();
                        EditText dateText = (EditText) view.findViewById(R.id.search_date);
                        String date = dateText.getText().toString();
                        historyItems = DBManager.getInstance(historyView.getContext()).search(comment, date);
                        adapter = new HistoryAdapter(historyItems);
                        historyView.setAdapter(adapter);
                    }
                });
        AlertDialog alert = searchBuilder.create();
        alert.show();
    }

    public void clearItems() {
        AlertDialog.Builder clearBuilder = new AlertDialog.Builder(historyView.getContext());
        clearBuilder
                .setMessage(R.string.confirm_clear_title)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.getInstance(historyView.getContext()).deleteAll();
                        historyItems.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
        AlertDialog alert = clearBuilder.create();
        alert.show();
    }

    public void OnDateTextClick(View view) {
        final EditText dateText = (EditText) view.findViewById(R.id.search_date);
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
                String date = sdf.format(calendar.getTime());
                dateText.setText(date);
            }
        };

        DatePickerDialog picker = new DatePickerDialog(view.getContext(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }
}
