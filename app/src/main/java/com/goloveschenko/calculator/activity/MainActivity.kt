package com.goloveschenko.calculator.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.goloveschenko.calculator.R
import com.goloveschenko.calculator.action.Calculator
import com.goloveschenko.calculator.action.Converter
import com.goloveschenko.calculator.action.Notation
import com.goloveschenko.calculator.action.Operation
import com.goloveschenko.calculator.dao.manager.DBManager
import com.goloveschenko.calculator.entity.HistoryItem
import com.goloveschenko.calculator.fragment.FragmentBin
import com.goloveschenko.calculator.fragment.FragmentDec
import com.goloveschenko.calculator.fragment.FragmentHex
import com.goloveschenko.calculator.fragment.FragmentOct
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var operation: Operation = Operation.NONE
    private var notation: Notation = Notation.DEC
    private var curNumber: BigDecimal = BigDecimal.ZERO

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_history -> {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivityForResult(intent, RESULT_CODE)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            val expr = data.getStringExtra(HistoryActivity.EXTRA_EXPRESSION_RESULT)
            inputText.setText(expr)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EXPRESSION, expressionText.text.toString())
        outState.putString(KEY_NOTATION, notation.toString())
        outState.putString(KEY_OPERATION, operation.toString())
        outState.putString(KEY_NUMBER, curNumber.toString())
    }

    override fun onClick(v: View) {
        val text: String
        val number: BigDecimal

        when (v.id) {
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.buttonA,
            R.id.buttonB,
            R.id.buttonC,
            R.id.buttonD,
            R.id.buttonE,
            R.id.buttonF,
            R.id.buttonDevider -> {
                val button = v as Button
                val expStr: String
                if (inputText.text.toString() != "0" || v.getId() == R.id.buttonDevider) {
                    expStr = inputText.text.toString() + button.text.toString()
                } else {
                    expStr = button.text.toString()
                }
                inputText.setText(expStr)
                inputText.setSelection(expStr.length)
            }
            R.id.buttonDelete -> {
                text = inputText.text.toString()
                if (text.length > 1) {
                    inputText.setText(text.substring(0, text.length - 1))
                    inputText.setSelection(text.length - 1)
                } else {
                    inputText.setText("0")
                    inputText.setSelection(1)
                    curNumber = BigDecimal.ZERO
                }
            }
            R.id.buttonPlus -> {
                text = inputText.text.toString()
                if (text.isEmpty()) {
                    return
                }
                expressionText.text = expressionText.text.toString() + text + SYMBOL_PLUS

                number = Converter.stringToValue(notation, text)
                inputText.setText("")
                if (curNumber.compareTo(BigDecimal.ZERO) != 0) {
                    curNumber = Calculator.doOperation(operation, curNumber, number)
                } else {
                    curNumber = number
                }

                operation = Operation.PLUS
            }
            R.id.buttonMinus -> {
                text = inputText.text.toString()
                if (text.isEmpty()) {
                    return
                }
                expressionText.text = expressionText.text.toString() + text + SYMBOL_MINUS

                number = Converter.stringToValue(notation, text)
                inputText.setText("")
                if (curNumber.compareTo(BigDecimal.ZERO) != 0) {
                    curNumber = Calculator.doOperation(operation, curNumber, number)
                } else {
                    curNumber = number
                }

                operation = Operation.MINUS
            }
            R.id.buttonMultiply -> {
                text = inputText.text.toString()
                if (text.isEmpty()) {
                    return
                }
                expressionText.text = expressionText.text.toString() + text + SYMBOL_MULTIPLY

                number = Converter.stringToValue(notation, text)
                inputText.setText("")
                if (curNumber.compareTo(BigDecimal.ZERO) != 0) {
                    curNumber = Calculator.doOperation(operation, curNumber, number)
                } else {
                    curNumber = number
                }

                operation = Operation.MULTIPLY
            }
            R.id.buttonDevide -> {
                text = inputText.text.toString()
                if (text.isEmpty()) {
                    return
                }
                expressionText.text = expressionText.text.toString() + text + SYMBOL_DEVIDE

                number = Converter.stringToValue(notation, text)
                inputText.setText("")
                if (curNumber.compareTo(BigDecimal.ZERO) != 0) {
                    curNumber = Calculator.doOperation(operation, curNumber, number)
                } else {
                    curNumber = number
                }

                operation = Operation.DEVIDE
            }
            R.id.buttonPower -> {
                text = inputText.text.toString()
                if (text.isEmpty()) {
                    return
                }
                expressionText.text = expressionText.text.toString() + text + SYMBOL_POWER

                number = BigDecimal(text)
                inputText.setText("")
                if (curNumber.compareTo(BigDecimal.ZERO) != 0) {
                    curNumber = Calculator.doOperation(operation, curNumber, number)
                } else {
                    curNumber = number
                }

                operation = Operation.POWER
            }
            R.id.buttonResult -> {
                text = inputText.text.toString()
                if (text.isEmpty()) {
                    return
                }

                val resultNumber: BigDecimal
                if (operation != Operation.NONE) {
                    val lastNumber = Converter.stringToValue(notation, text)
                    resultNumber = Calculator.doOperation(operation, curNumber, lastNumber)
                } else {
                    resultNumber = Calculator.parse(text)
                }
                val result = Converter.valueToString(notation, resultNumber)
                inputText.setText(result)

                val historyItem = HistoryItem(null,
                        SimpleDateFormat(HistoryActivity.DATE_FORMAT, Locale.US).format(Date()),
                        expressionText.text.toString() + text,
                        result,
                        null)
                DBManager.getInstance(applicationContext).insertValue(historyItem)

                curNumber = BigDecimal.ZERO
                operation = Operation.NONE
                expressionText.text = ""
                inputText.setSelection(result.length)
            }
        }
    }

    private fun updateInputText(newNotation: Notation) {
        val oldText = inputText.text.toString()
        val newText = Converter.convertByNotation(notation, newNotation, oldText)
        inputText.setText(newText)
        inputText.setSelection(inputText.text.length)
    }

    private fun initRadioButtons() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radioButtonHex -> {
                        updateInputText(Notation.HEX)
                        notation = Notation.HEX
                    }
                    R.id.radioButtonDec -> {
                        updateInputText(Notation.DEC)
                        notation = Notation.DEC
                    }
                    R.id.radioButtonOct -> {
                        updateInputText(Notation.OCT)
                        notation = Notation.OCT
                    }
                    R.id.radioButtonBin -> {
                        updateInputText(Notation.BIN)
                        notation = Notation.BIN
                    }
                }
                initFragment(notation)
            }

            //first checked DEC
            radioButtonDec.isChecked = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            expressionText.text = savedInstanceState.getString(KEY_EXPRESSION)
            notation = Notation.valueOf(savedInstanceState.getString(KEY_NOTATION))
            operation = Operation.valueOf(savedInstanceState.getString(KEY_OPERATION))
            curNumber = BigDecimal(savedInstanceState.getString(KEY_NUMBER))
        } else {
            curNumber = BigDecimal.ZERO
            notation = Notation.DEC
            operation = Operation.NONE
        }

        initFragment(Notation.DEC)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initRadioButtons()
        }

        inputText.setSelection(1)
    }

    private fun initFragment(notation: Notation) {
        var fragment: Fragment? = null
        when (notation) {
            Notation.BIN -> fragment = FragmentBin()
            Notation.OCT -> fragment = FragmentOct()
            Notation.DEC -> fragment = FragmentDec()
            Notation.HEX -> fragment = FragmentHex()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentCont, fragment)
                .commit()
    }

    companion object {
        val RESULT_CODE = 200

        val SYMBOL_PLUS = " + "
        val SYMBOL_MINUS = " - "
        val SYMBOL_MULTIPLY = " * "
        val SYMBOL_DEVIDE = " / "
        val SYMBOL_POWER = " ^ "

        val KEY_EXPRESSION = "expression"
        val KEY_NOTATION = "notation"
        val KEY_OPERATION = "operation"
        val KEY_NUMBER = "number"
    }
}
