package com.goloveschenko.example.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.goloveschenko.example.action.Calculator;
import com.goloveschenko.example.action.Converter;
import com.goloveschenko.example.action.Notation;
import com.goloveschenko.example.action.Operation;
import com.goloveschenko.example.R;
import com.goloveschenko.example.dao.manager.DBManager;
import com.goloveschenko.example.entity.HistoryItem;
import com.goloveschenko.example.fragment.FragmentBin;
import com.goloveschenko.example.fragment.FragmentDec;
import com.goloveschenko.example.fragment.FragmentHex;
import com.goloveschenko.example.fragment.FragmentOct;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_CODE = 200;

    public final static String SYMBOL_PLUS = " + ";
    public final static String SYMBOL_MINUS = " - ";
    public final static String SYMBOL_MULTIPLY = " * ";
    public final static String SYMBOL_DEVIDE = " / ";
    public final static String SYMBOL_POWER = " ^ ";

    public final static String KEY_EXPRESSION = "expression";
    public final static String KEY_NOTATION = "notation";
    public final static String KEY_OPERATION = "operation";
    public final static String KEY_NUMBER = "number";

    private Operation operation;
    private Notation notation;
    private BigDecimal curNumber;
    private EditText inputText;
    private TextView expressionText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivityForResult(intent, RESULT_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String expr = data.getStringExtra(HistoryActivity.EXTRA_EXPRESSION_RESULT);
            inputText.setText(expr);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_EXPRESSION, expressionText.getText().toString());
        outState.putString(KEY_NOTATION, notation.toString());
        outState.putString(KEY_OPERATION, operation.toString());
        outState.putString(KEY_NUMBER, curNumber.toString());
    }

    @Override
    public void onClick(View v) {
        String text;
        BigDecimal number;

        switch (v.getId()) {
            case R.id.button0:
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
            case R.id.buttonA:
            case R.id.buttonB:
            case R.id.buttonC:
            case R.id.buttonD:
            case R.id.buttonE:
            case R.id.buttonF:
            case R.id.buttonDevider:
                Button button = (Button) v;
                String expStr;
                if (!inputText.getText().toString().equals("0") || v.getId() == R.id.buttonDevider) {
                    expStr = inputText.getText() + button.getText().toString();
                } else {
                    expStr = button.getText().toString();
                }
                inputText.setText(expStr);
                inputText.setSelection(expStr.length());
                break;
            case R.id.buttonDelete:
                text = inputText.getText().toString();
                if (text.length() > 1) {
                    inputText.setText(text.substring(0, text.length() - 1));
                    inputText.setSelection(text.length() - 1);
                } else {
                    inputText.setText("0");
                    inputText.setSelection(1);
                    curNumber = BigDecimal.ZERO;
                }
                break;
            case R.id.buttonPlus:
                text = inputText.getText().toString();
                if (text.isEmpty()){
                    return;
                }
                expressionText.setText(expressionText.getText() + text + SYMBOL_PLUS);

                number = Converter.stringToValue(notation, text);
                inputText.setText("");
                if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                    curNumber = Calculator.doOperation(operation, curNumber, number);
                } else {
                    curNumber = number;
                }

                operation = Operation.PLUS;
                break;
            case R.id.buttonMinus:
                text = inputText.getText().toString();
                if (text.isEmpty()){
                    return;
                }
                expressionText.setText(expressionText.getText() + text + SYMBOL_MINUS);

                number = Converter.stringToValue(notation, text);
                inputText.setText("");
                if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                    curNumber = Calculator.doOperation(operation, curNumber, number);
                } else {
                    curNumber = number;
                }

                operation = Operation.MINUS;
                break;
            case R.id.buttonMultiply:
                text = inputText.getText().toString();
                if (text.isEmpty()){
                    return;
                }
                expressionText.setText(expressionText.getText() + text + SYMBOL_MULTIPLY);

                number = Converter.stringToValue(notation, text);
                inputText.setText("");
                if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                    curNumber = Calculator.doOperation(operation, curNumber, number);
                } else {
                    curNumber = number;
                }

                operation = Operation.MULTIPLY;
                break;
            case R.id.buttonDevide:
                text = inputText.getText().toString();
                if (text.isEmpty()){
                    return;
                }
                expressionText.setText(expressionText.getText() + text + SYMBOL_DEVIDE);

                number = Converter.stringToValue(notation, text);
                inputText.setText("");
                if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                    curNumber = Calculator.doOperation(operation, curNumber, number);
                } else {
                    curNumber = number;
                }

                operation = Operation.DEVIDE;
                break;
            case R.id.buttonPower:
                text = inputText.getText().toString();
                if (text.isEmpty()){
                    return;
                }
                expressionText.setText(expressionText.getText() + text + SYMBOL_POWER);

                number = new BigDecimal(text);
                inputText.setText("");
                if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                    curNumber = Calculator.doOperation(operation, curNumber, number);
                } else {
                    curNumber = number;
                }

                operation = Operation.POWER;
                break;
            case R.id.buttonResult:
                text = inputText.getText().toString();
                if (text.isEmpty()){
                    return;
                }

                BigDecimal resultNumber;
                if (operation != Operation.NONE) {
                    BigDecimal lastNumber = Converter.stringToValue(notation, text);
                    resultNumber = Calculator.doOperation(operation, curNumber, lastNumber);
                } else {
                    resultNumber = Calculator.parse(text);
                }
                String result = Converter.valueToString(notation, resultNumber);
                inputText.setText(result);

                HistoryItem item = new HistoryItem();
                item.setExpression(expressionText.getText().toString() + text);
                item.setResult(result);
                DBManager.getInstance(getApplicationContext()).insertValue(item);

                curNumber = BigDecimal.ZERO;
                operation = Operation.NONE;
                expressionText.setText("");
                inputText.setSelection(result.length());
                break;
        }
    }

    private void updateInputText(Notation newNotation){
        String oldText = inputText.getText().toString();
        String newText = Converter.convertByNotation(notation, newNotation, oldText);
        inputText.setText(newText);
        inputText.setSelection(inputText.getText().length());
    }

    private void initRadioButtons(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    switch (checkedId) {
                        case R.id.radioButtonHex:
                            updateInputText(Notation.HEX);
                            notation = Notation.HEX;
                            break;
                        case R.id.radioButtonDec:
                            updateInputText(Notation.DEC);
                            notation = Notation.DEC;
                            break;
                        case R.id.radioButtonOct:
                            updateInputText(Notation.OCT);
                            notation = Notation.OCT;
                            break;
                        case R.id.radioButtonBin:
                            updateInputText(Notation.BIN);
                            notation = Notation.BIN;
                            break;
                    }
                    initFragment(notation);
                }
            });

            //first checked DEC
            RadioButton radioButton = (RadioButton) findViewById(R.id.radioButtonDec);
            radioButton.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = (EditText) findViewById(R.id.editText);
        expressionText = (TextView) findViewById(R.id.textExpressionView);
        if (savedInstanceState != null) {
            expressionText.setText(savedInstanceState.getString(KEY_EXPRESSION));
            notation = Notation.valueOf(savedInstanceState.getString(KEY_NOTATION));
            operation = Operation.valueOf(savedInstanceState.getString(KEY_OPERATION));
            curNumber = new BigDecimal(savedInstanceState.getString(KEY_NUMBER));
        } else {
            curNumber = BigDecimal.ZERO;
            notation = Notation.DEC;
            operation = Operation.NONE;
        }

        initFragment(Notation.DEC);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initRadioButtons();
        }

        inputText.setSelection(1);
    }

    private void initFragment(Notation notation) {
        Fragment fragment = null;
        switch (notation) {
            case BIN:
                fragment = new FragmentBin();
                break;
            case OCT:
                fragment = new FragmentOct();
                break;
            case DEC:
                fragment = new FragmentDec();
                break;
            case HEX:
                fragment = new FragmentHex();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentCont, fragment)
                .commit();
    }
}
