package com.goloveschenko.calculator.action

import java.math.BigDecimal
import java.util.*

object Calculator {

    private val MAIN_MATH_OPERATIONS: MutableMap<String, Int>

    init {
        MAIN_MATH_OPERATIONS = HashMap<String, Int>()
        MAIN_MATH_OPERATIONS.put("*", 1)
        MAIN_MATH_OPERATIONS.put("/", 1)
        MAIN_MATH_OPERATIONS.put("+", 2)
        MAIN_MATH_OPERATIONS.put("-", 2)
    }

    private fun sortingStation(expression: String): String {
        var expression = expression
        val leftBracket = "("
        val rightBracket = ")"

        val out = ArrayList<String>()
        val stack = Stack<String>()

        expression = expression.replace(" ", "")

        val operationSymbols = HashSet(MAIN_MATH_OPERATIONS.keys)
        operationSymbols.add(leftBracket)
        operationSymbols.add(rightBracket)

        var index = 0
        var findNext = true
        while (findNext) {
            var nextOperationIndex = expression.length
            var nextOperation = ""

            for (operation in operationSymbols) {
                val i = expression.indexOf(operation, index)
                if (i >= 0 && i < nextOperationIndex) {
                    nextOperation = operation
                    nextOperationIndex = i
                }
            }
            if (nextOperationIndex == expression.length) {
                findNext = false
            } else {
                if (index != nextOperationIndex) {
                    out.add(expression.substring(index, nextOperationIndex))
                }
                if (nextOperation == leftBracket) {
                    stack.push(nextOperation)
                } else if (nextOperation == rightBracket) {
                    while (stack.peek() != leftBracket) {
                        out.add(stack.pop())
                        if (stack.empty()) {
                            throw IllegalArgumentException("Unmatched brackets")
                        }
                    }
                    stack.pop()
                } else {
                    while (!stack.empty() && stack.peek() != leftBracket &&
                            MAIN_MATH_OPERATIONS.getValue(nextOperation) >= MAIN_MATH_OPERATIONS.getValue(stack.peek())) {
                        out.add(stack.pop())
                    }
                    stack.push(nextOperation)
                }
                index = nextOperationIndex + nextOperation.length
            }
        }
        if (index != expression.length) {
            out.add(expression.substring(index))
        }
        while (!stack.empty()) {
            out.add(stack.pop())
        }
        val result = StringBuilder()
        if (!out.isEmpty())
            result.append(out.removeAt(0))
        while (!out.isEmpty())
            result.append(" ").append(out.removeAt(0))

        return result.toString()
    }

    fun parse(expression: String): BigDecimal {
        val rpn = sortingStation(expression)
        val tokenizer = StringTokenizer(rpn, " ")
        val stack = Stack<BigDecimal>()
        while (tokenizer.hasMoreTokens()) {
            val token = tokenizer.nextToken()
            if (!MAIN_MATH_OPERATIONS.keys.contains(token)) {
                stack.push(BigDecimal(token))
            } else {
                val operand2: BigDecimal
                operand2 = stack.pop()
                val operand1 = if (stack.empty()) BigDecimal.ZERO else stack.pop()
                val res: BigDecimal

                when (token) {
                    "+" -> {
                        res = doOperation(Operation.PLUS, operand1, operand2)
                        stack.push(res)
                    }
                    "-" -> {
                        res = doOperation(Operation.MINUS, operand1, operand2)
                        stack.push(res)
                    }
                    "*" -> {
                        res = doOperation(Operation.MULTIPLY, operand1, operand2)
                        stack.push(res)
                    }
                    "/" -> {
                        res = doOperation(Operation.DEVIDE, operand1, operand2)
                        stack.push(res)
                    }
                }
            }
        }
        return stack.pop()
    }

    fun doOperation(operation: Operation, num1: BigDecimal, num2: BigDecimal): BigDecimal {
        try {
            when (operation) {
                Operation.PLUS -> return num1.add(num2)
                Operation.MINUS -> return num1.subtract(num2)
                Operation.MULTIPLY -> return num1.multiply(num2)
                Operation.DEVIDE -> return num1.divide(num2, 3, BigDecimal.ROUND_HALF_DOWN)
                Operation.POWER -> return num1.pow(num2.toInt())
                else -> return BigDecimal.ZERO
            }
        } catch (e: ArithmeticException) {
            return BigDecimal.ZERO
        }

    }
}
