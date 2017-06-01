package com.goloveschenko.example.action;

import java.math.BigDecimal;

public class Converter {
    public static BigDecimal stringToValue(Notation notation, String text){
        switch (notation) {
            case BIN:
                return new BigDecimal(Integer.parseInt(text, 2));
            case OCT:
                return new BigDecimal(Integer.parseInt(text, 8));
            case DEC:
                return new BigDecimal(text);
            case HEX:
                return new BigDecimal(Integer.parseInt(text, 16));
            default:
                return BigDecimal.ZERO;
        }
    }

    public static String valueToString(Notation notation, BigDecimal number){
        switch (notation) {
            case BIN:
                return number.toBigInteger().toString(2);
            case OCT:
                return number.toBigInteger().toString(8);
            case DEC:
                return number.toString();
            case HEX:
                return number.toBigInteger().toString(16).toUpperCase();
            default:
                return "";
        }
    }

    public static String convertByNotation(Notation oldnotation, Notation newnotation, String text){
        BigDecimal number = stringToValue(oldnotation, text);
        return valueToString(newnotation, number);
    }
}
