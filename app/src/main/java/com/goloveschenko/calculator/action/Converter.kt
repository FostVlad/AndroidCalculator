package com.goloveschenko.calculator.action

import java.math.BigDecimal

object Converter {
    fun stringToValue(notation: Notation, text: String): BigDecimal {
        when (notation) {
            Notation.BIN -> return BigDecimal(Integer.parseInt(text, 2))
            Notation.OCT -> return BigDecimal(Integer.parseInt(text, 8))
            Notation.DEC -> return BigDecimal(text)
            Notation.HEX -> return BigDecimal(Integer.parseInt(text, 16))
            else -> return BigDecimal.ZERO
        }
    }

    fun valueToString(notation: Notation, number: BigDecimal): String {
        when (notation) {
            Notation.BIN -> return number.toBigInteger().toString(2)
            Notation.OCT -> return number.toBigInteger().toString(8)
            Notation.DEC -> return number.toString()
            Notation.HEX -> return number.toBigInteger().toString(16).toUpperCase()
            else -> return ""
        }
    }

    fun convertByNotation(oldnotation: Notation, newnotation: Notation, text: String): String {
        val number = stringToValue(oldnotation, text)
        return valueToString(newnotation, number)
    }
}
