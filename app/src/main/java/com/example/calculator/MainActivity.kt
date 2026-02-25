package com.example.calculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var expression = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.tvDisplay)

        val digitIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnDot
        )

        digitIds.forEach { id ->
            findViewById<MaterialButton>(id).setOnClickListener {
                val value = (it as MaterialButton).text.toString()
                expression.append(value)
                updateDisplay()
            }
        }

        listOf(R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv).forEach { id ->
            findViewById<MaterialButton>(id).setOnClickListener {
                val op = (it as MaterialButton).text.toString()
                if (expression.isNotEmpty() && expression.last() !in "+-×÷") {
                    expression.append(op)
                    updateDisplay()
                }
            }
        }

        findViewById<MaterialButton>(R.id.btnClear).setOnClickListener {
            expression.clear()
            updateDisplay()
        }

        findViewById<MaterialButton>(R.id.btnDelete).setOnClickListener {
            if (expression.isNotEmpty()) {
                expression.deleteAt(expression.lastIndex)
                updateDisplay()
            }
        }

        findViewById<MaterialButton>(R.id.btnEquals).setOnClickListener {
            val result = evaluate(expression.toString())
            expression.clear()
            expression.append(result)
            updateDisplay()
        }

        updateDisplay()
    }

    private fun updateDisplay() {
        display.text = if (expression.isEmpty()) "0" else expression.toString()
    }

    private fun evaluate(input: String): String {
        return try {
            val sanitized = input
                .replace("×", "*")
                .replace("÷", "/")
            val tokens = tokenize(sanitized)
            val value = compute(tokens)
            if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
        } catch (_: Exception) {
            "Error"
        }
    }

    private fun tokenize(expr: String): List<String> {
        if (expr.isBlank()) throw IllegalArgumentException("Empty")
        val tokens = mutableListOf<String>()
        val number = StringBuilder()

        for (c in expr) {
            if (c.isDigit() || c == '.') {
                number.append(c)
            } else if (c in "+-*/") {
                if (number.isEmpty()) throw IllegalArgumentException("Bad format")
                tokens.add(number.toString())
                number.clear()
                tokens.add(c.toString())
            } else {
                throw IllegalArgumentException("Invalid char")
            }
        }

        if (number.isNotEmpty()) tokens.add(number.toString())
        if (tokens.last() in listOf("+", "-", "*", "/")) throw IllegalArgumentException("Trailing op")

        return tokens
    }

    private fun compute(tokens: List<String>): Double {
        val stack = mutableListOf<Double>()
        var currentOp = "+"
        var i = 0

        while (i < tokens.size) {
            val token = tokens[i]
            if (token in listOf("+", "-", "*", "/")) {
                currentOp = token
            } else {
                val number = token.toDouble()
                when (currentOp) {
                    "+" -> stack.add(number)
                    "-" -> stack.add(-number)
                    "*" -> stack[stack.lastIndex] = stack.last() * number
                    "/" -> {
                        if (number == 0.0) throw ArithmeticException("Divide by zero")
                        stack[stack.lastIndex] = stack.last() / number
                    }
                }
            }
            i++
        }

        return stack.sum()
    }
}
