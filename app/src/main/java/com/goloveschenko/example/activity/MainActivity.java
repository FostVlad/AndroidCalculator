package com.goloveschenko.example.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.goloveschenko.example.action.Converter;
import com.goloveschenko.example.action.Notation;
import com.goloveschenko.example.action.Operation;
import com.goloveschenko.example.R;
import com.goloveschenko.example.dao.manager.DBManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public final static String SYMBOL_PLUS = " + ";
    public final static String SYMBOL_MINUS = " - ";
    public final static String SYMBOL_MULTIPLY = " * ";
    public final static String SYMBOL_DEVIDE = " / ";
    public final static String SYMBOL_POWER = " ^ ";
    public final static String SYMBOL_EQUALS = " = ";

    private HashMap<Integer, Button> buttonsMap;

    private Operation operation;
    private Notation notation;
    private BigDecimal curNumber;
    private EditText inputText;
    private TextView expressionText;

    private void setListenerForButton(int idButton){
        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button currentBtn = (Button) v;
                String expStr;
                if (!inputText.getText().toString().equals("0")) {
                    expStr = inputText.getText() + currentBtn.getText().toString();
                } else {
                    expStr = currentBtn.getText().toString();
                }
                inputText.setText(expStr);
                inputText.setSelection(expStr.length());
            }
        };

        Button button = buttonsMap.get(idButton);
        switch (idButton) {
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
                button.setOnClickListener(buttonListener);
                break;
            case R.id.buttonDelete:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = inputText.getText().toString();
                        if (text.length() > 1) {
                            inputText.setText(text.substring(0, text.length() - 1));
                            inputText.setSelection(text.length() - 1);
                        } else {
                            inputText.setText("0");
                            inputText.setSelection(1);
                            curNumber = BigDecimal.ZERO;
                        }
                    }
                });
                break;
            case R.id.buttonPlus:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = inputText.getText().toString();
                        if (text.isEmpty()){
                            return;
                        }
                        expressionText.setText(expressionText.getText() + text + SYMBOL_PLUS);

                        BigDecimal number = Converter.stringToValue(notation, text);
                        inputText.setText("");
                        if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                            curNumber = doOperation(curNumber, number);
                        } else {
                            curNumber = number;
                        }

                        operation = Operation.PLUS;
                    }
                });
                break;
            case R.id.buttonMinus:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = inputText.getText().toString();
                        if (text.isEmpty()){
                            return;
                        }
                        expressionText.setText(expressionText.getText() + text + SYMBOL_MINUS);

                        BigDecimal number = Converter.stringToValue(notation, text);
                        inputText.setText("");
                        if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                            curNumber = doOperation(curNumber, number);
                        } else {
                            curNumber = number;
                        }

                        operation = Operation.MINUS;
                    }
                });
                break;
            case R.id.buttonMultiply:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = inputText.getText().toString();
                        if (text.isEmpty()){
                            return;
                        }
                        expressionText.setText(expressionText.getText() + text + SYMBOL_MULTIPLY);

                        BigDecimal number = Converter.stringToValue(notation, text);
                        inputText.setText("");
                        if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                            curNumber = doOperation(curNumber, number);
                        } else {
                            curNumber = number;
                        }

                        operation = Operation.MULTIPLY;
                    }
                });
                break;
            case R.id.buttonDevide:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = inputText.getText().toString();
                        if (text.isEmpty()){
                            return;
                        }
                        expressionText.setText(expressionText.getText() + text + SYMBOL_DEVIDE);

                        BigDecimal number = Converter.stringToValue(notation, text);
                        inputText.setText("");
                        if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                            curNumber = doOperation(curNumber, number);
                        } else {
                            curNumber = number;
                        }

                        operation = Operation.DEVIDE;
                    }
                });
                break;
            case R.id.buttonPower:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = inputText.getText().toString();
                        if (text.isEmpty()){
                            return;
                        }
                        expressionText.setText(expressionText.getText() + text + SYMBOL_POWER);

                        BigDecimal number = new BigDecimal(text);
                        inputText.setText("");
                        if (curNumber.compareTo(BigDecimal.ZERO) != 0){
                            curNumber = doOperation(curNumber, number);
                        } else {
                            curNumber = number;
                        }

                        operation = Operation.POWER;
                    }
                });
                break;
            case R.id.buttonResult:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = inputText.getText().toString();
                        if (text.isEmpty()){
                            return;
                        }

                        BigDecimal lastNumber = Converter.stringToValue(notation, text);
                        BigDecimal resultNumber = doOperation(curNumber, lastNumber);
                        String resultText = Converter.valueToString(notation, resultNumber);

                        if (resultText.contains(".")) {
                            inputText.setText(String.format("%.3f", resultText));
                        } else {
                            inputText.setText(resultText);
                        }

                        //history
                        String expression = expressionText.getText().toString() + text + SYMBOL_EQUALS + resultText;   //fix it
                        DBManager.getInstance(getApplicationContext()).insertValue(expression, new Date().toString());

                        curNumber = BigDecimal.ZERO;
                        expressionText.setText("");
                        inputText.setSelection(resultText.length());
                    }
                });
                break;
        }
    }

    private BigDecimal doOperation(BigDecimal num1, BigDecimal num2){
        switch (operation){
            case PLUS:
                return num1.add(num2);
            case MINUS:
                return num1.subtract(num2);
            case MULTIPLY:
                return num1.multiply(num2);
            case DEVIDE:
                return num1.divide(num2, 3, BigDecimal.ROUND_HALF_DOWN);
            case POWER:
                return num1.pow(num2.intValue());
            default:
                return BigDecimal.ZERO;
        }
    }

    private void updateInputText(Notation newNotation){
        String oldText = inputText.getText().toString();
        String newText = Converter.convertByNotation(notation, newNotation, oldText);
        inputText.setText(newText);
        inputText.setSelection(inputText.getText().length());
    }

    private void inicializeButtons(){
        //SYMBOLS
        setListenerForButton(R.id.button0);
        setListenerForButton(R.id.button1);
        setListenerForButton(R.id.button2);
        setListenerForButton(R.id.button3);
        setListenerForButton(R.id.button4);
        setListenerForButton(R.id.button5);
        setListenerForButton(R.id.button6);
        setListenerForButton(R.id.button7);
        setListenerForButton(R.id.button8);
        setListenerForButton(R.id.button9);
        setListenerForButton(R.id.buttonDevider);

        //OPERATIONS
        setListenerForButton(R.id.buttonDelete);
        setListenerForButton(R.id.buttonMultiply);
        setListenerForButton(R.id.buttonDevide);
        setListenerForButton(R.id.buttonPlus);
        setListenerForButton(R.id.buttonMinus);
        setListenerForButton(R.id.buttonPower);
        setListenerForButton(R.id.buttonResult);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setListenerForButton(R.id.buttonA);
            setListenerForButton(R.id.buttonB);
            setListenerForButton(R.id.buttonC);
            setListenerForButton(R.id.buttonD);
            setListenerForButton(R.id.buttonE);
            setListenerForButton(R.id.buttonF);

            //RADIO BUTTONS
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    switch (checkedId) {
                        case R.id.radioButtonHex:
                            updateInputText(Notation.HEX);

                            buttonsMap.get(R.id.button2).setEnabled(true);
                            buttonsMap.get(R.id.button3).setEnabled(true);
                            buttonsMap.get(R.id.button4).setEnabled(true);
                            buttonsMap.get(R.id.button5).setEnabled(true);
                            buttonsMap.get(R.id.button6).setEnabled(true);
                            buttonsMap.get(R.id.button7).setEnabled(true);
                            buttonsMap.get(R.id.button8).setEnabled(true);
                            buttonsMap.get(R.id.button9).setEnabled(true);
                            buttonsMap.get(R.id.buttonA).setEnabled(true);
                            buttonsMap.get(R.id.buttonB).setEnabled(true);
                            buttonsMap.get(R.id.buttonC).setEnabled(true);
                            buttonsMap.get(R.id.buttonD).setEnabled(true);
                            buttonsMap.get(R.id.buttonE).setEnabled(true);
                            buttonsMap.get(R.id.buttonF).setEnabled(true);

                            notation = Notation.HEX;
                            break;
                        case R.id.radioButtonDec:
                            updateInputText(Notation.DEC);

                            buttonsMap.get(R.id.button2).setEnabled(true);
                            buttonsMap.get(R.id.button3).setEnabled(true);
                            buttonsMap.get(R.id.button4).setEnabled(true);
                            buttonsMap.get(R.id.button5).setEnabled(true);
                            buttonsMap.get(R.id.button6).setEnabled(true);
                            buttonsMap.get(R.id.button7).setEnabled(true);
                            buttonsMap.get(R.id.button8).setEnabled(true);
                            buttonsMap.get(R.id.button9).setEnabled(true);
                            buttonsMap.get(R.id.buttonA).setEnabled(false);
                            buttonsMap.get(R.id.buttonB).setEnabled(false);
                            buttonsMap.get(R.id.buttonC).setEnabled(false);
                            buttonsMap.get(R.id.buttonD).setEnabled(false);
                            buttonsMap.get(R.id.buttonE).setEnabled(false);
                            buttonsMap.get(R.id.buttonF).setEnabled(false);

                            notation = Notation.DEC;
                            break;
                        case R.id.radioButtonOct:
                            updateInputText(Notation.OCT);

                            buttonsMap.get(R.id.button2).setEnabled(true);
                            buttonsMap.get(R.id.button3).setEnabled(true);
                            buttonsMap.get(R.id.button4).setEnabled(true);
                            buttonsMap.get(R.id.button5).setEnabled(true);
                            buttonsMap.get(R.id.button6).setEnabled(true);
                            buttonsMap.get(R.id.button7).setEnabled(true);
                            buttonsMap.get(R.id.button8).setEnabled(false);
                            buttonsMap.get(R.id.button9).setEnabled(false);
                            buttonsMap.get(R.id.buttonA).setEnabled(false);
                            buttonsMap.get(R.id.buttonB).setEnabled(false);
                            buttonsMap.get(R.id.buttonC).setEnabled(false);
                            buttonsMap.get(R.id.buttonD).setEnabled(false);
                            buttonsMap.get(R.id.buttonE).setEnabled(false);
                            buttonsMap.get(R.id.buttonF).setEnabled(false);

                            notation = Notation.OCT;
                            break;
                        case R.id.radioButtonBin:
                            updateInputText(Notation.BIN);

                            buttonsMap.get(R.id.button2).setEnabled(false);
                            buttonsMap.get(R.id.button3).setEnabled(false);
                            buttonsMap.get(R.id.button4).setEnabled(false);
                            buttonsMap.get(R.id.button5).setEnabled(false);
                            buttonsMap.get(R.id.button6).setEnabled(false);
                            buttonsMap.get(R.id.button7).setEnabled(false);
                            buttonsMap.get(R.id.button8).setEnabled(false);
                            buttonsMap.get(R.id.button9).setEnabled(false);
                            buttonsMap.get(R.id.buttonA).setEnabled(false);
                            buttonsMap.get(R.id.buttonB).setEnabled(false);
                            buttonsMap.get(R.id.buttonC).setEnabled(false);
                            buttonsMap.get(R.id.buttonD).setEnabled(false);
                            buttonsMap.get(R.id.buttonE).setEnabled(false);
                            buttonsMap.get(R.id.buttonF).setEnabled(false);

                            notation = Notation.BIN;
                            break;
                    }
                }
            });

            buttonsMap.get(R.id.buttonDevider).setEnabled(false);

            //first checked DEC
            RadioButton radioButton = (RadioButton) findViewById(R.id.radioButtonDec);
            radioButton.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        curNumber = BigDecimal.ZERO;
        notation = Notation.DEC;
        inputText = (EditText) findViewById(R.id.editText);
        expressionText = (TextView) findViewById(R.id.textExpressionView);

        buttonsMap = new HashMap<>();
        buttonsMap.put(R.id.button0, (Button) findViewById(R.id.button0));
        buttonsMap.put(R.id.button1, (Button) findViewById(R.id.button1));
        buttonsMap.put(R.id.button2, (Button) findViewById(R.id.button2));
        buttonsMap.put(R.id.button3, (Button) findViewById(R.id.button3));
        buttonsMap.put(R.id.button4, (Button) findViewById(R.id.button4));
        buttonsMap.put(R.id.button5, (Button) findViewById(R.id.button5));
        buttonsMap.put(R.id.button6, (Button) findViewById(R.id.button6));
        buttonsMap.put(R.id.button7, (Button) findViewById(R.id.button7));
        buttonsMap.put(R.id.button8, (Button) findViewById(R.id.button8));
        buttonsMap.put(R.id.button9, (Button) findViewById(R.id.button9));
        buttonsMap.put(R.id.buttonDevider, (Button) findViewById(R.id.buttonDevider));
        buttonsMap.put(R.id.buttonDelete, (Button) findViewById(R.id.buttonDelete));
        buttonsMap.put(R.id.buttonMultiply, (Button) findViewById(R.id.buttonMultiply));
        buttonsMap.put(R.id.buttonDevide, (Button) findViewById(R.id.buttonDevide));
        buttonsMap.put(R.id.buttonPlus, (Button) findViewById(R.id.buttonPlus));
        buttonsMap.put(R.id.buttonMinus, (Button) findViewById(R.id.buttonMinus));
        buttonsMap.put(R.id.buttonPower, (Button) findViewById(R.id.buttonPower));
        buttonsMap.put(R.id.buttonResult, (Button) findViewById(R.id.buttonResult));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonsMap.put(R.id.buttonA, (Button) findViewById(R.id.buttonA));
            buttonsMap.put(R.id.buttonB, (Button) findViewById(R.id.buttonB));
            buttonsMap.put(R.id.buttonC, (Button) findViewById(R.id.buttonC));
            buttonsMap.put(R.id.buttonD, (Button) findViewById(R.id.buttonD));
            buttonsMap.put(R.id.buttonE, (Button) findViewById(R.id.buttonE));
            buttonsMap.put(R.id.buttonF, (Button) findViewById(R.id.buttonF));
        }

        //go to the history window
        inputText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                return true;
            }
        });

        inputText.setSelection(1);
        inicializeButtons();
    }
}
