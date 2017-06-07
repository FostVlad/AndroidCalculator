package com.goloveschenko.example.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {

    private static final Map<String, Integer> MAIN_MATH_OPERATIONS;

    static {
        MAIN_MATH_OPERATIONS = new HashMap<>();
        MAIN_MATH_OPERATIONS.put("*", 1);
        MAIN_MATH_OPERATIONS.put("/", 1);
        MAIN_MATH_OPERATIONS.put("+", 2);
        MAIN_MATH_OPERATIONS.put("-", 2);
    }

    private static String sortingStation(String expression) {
        String leftBracket = "(";
        String rightBracket = ")";

        List<String> out = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        expression = expression.replace(" ", "");

        Set<String> operationSymbols = new HashSet<>(MAIN_MATH_OPERATIONS.keySet());
        operationSymbols.add(leftBracket);
        operationSymbols.add(rightBracket);

        int index = 0;
        boolean findNext = true;
        while (findNext) {
            int nextOperationIndex = expression.length();
            String nextOperation = "";

            for (String operation : operationSymbols) {
                int i = expression.indexOf(operation, index);
                if (i >= 0 && i < nextOperationIndex) {
                    nextOperation = operation;
                    nextOperationIndex = i;
                }
            }
            if (nextOperationIndex == expression.length()) {
                findNext = false;
            } else {
                if (index != nextOperationIndex) {
                    out.add(expression.substring(index, nextOperationIndex));
                }
                if (nextOperation.equals(leftBracket)) {
                    stack.push(nextOperation);
                }
                else if (nextOperation.equals(rightBracket)) {
                    while (!stack.peek().equals(leftBracket)) {
                        out.add(stack.pop());
                        if (stack.empty()) {
                            throw new IllegalArgumentException("Unmatched brackets");
                        }
                    }
                    stack.pop();
                }
                else {
                    while (!stack.empty() && !stack.peek().equals(leftBracket) &&
                            (MAIN_MATH_OPERATIONS.get(nextOperation) >= MAIN_MATH_OPERATIONS.get(stack.peek()))) {
                        out.add(stack.pop());
                    }
                    stack.push(nextOperation);
                }
                index = nextOperationIndex + nextOperation.length();
            }
        }
        if (index != expression.length()) {
            out.add(expression.substring(index));
        }
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        StringBuilder result = new StringBuilder();
        if (!out.isEmpty())
            result.append(out.remove(0));
        while (!out.isEmpty())
            result.append(" ").append(out.remove(0));

        return result.toString();
    }

    public static BigDecimal parse(String expression) {
        String rpn = sortingStation(expression);
        StringTokenizer tokenizer = new StringTokenizer(rpn, " ");
        Stack<BigDecimal> stack = new Stack<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!MAIN_MATH_OPERATIONS.keySet().contains(token)) {
                stack.push(new BigDecimal(token));
            } else {
                BigDecimal operand2;
                operand2 = stack.pop();
                BigDecimal operand1 = stack.empty() ? BigDecimal.ZERO : stack.pop();
                BigDecimal res;

                switch (token) {
                    case "+":
                        res = doOperation(Operation.PLUS, operand1, operand2);
                        stack.push(res);
                        break;
                    case "-":
                        res = doOperation(Operation.MINUS, operand1, operand2);
                        stack.push(res);
                        break;
                    case "*":
                        res = doOperation(Operation.MULTIPLY, operand1, operand2);
                        stack.push(res);
                        break;
                    case "/":
                        res = doOperation(Operation.DEVIDE, operand1, operand2);
                        stack.push(res);
                        break;
                }
            }
        }
        return stack.pop();
    }

    public static BigDecimal doOperation(Operation operation , BigDecimal num1, BigDecimal num2) {
        try {
            switch (operation) {
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
        } catch (ArithmeticException e) {
            return BigDecimal.ZERO;
        }
    }
}
