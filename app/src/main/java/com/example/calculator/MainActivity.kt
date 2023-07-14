package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var valueTextView: TextView
    private val decimalFormat = DecimalFormat("#.##########")
    private var expression: String = "0"
    private var initialFlag: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        valueTextView = findViewById(R.id.value)

        val numberButtons = listOf<Button>(
            findViewById(R.id.number0), findViewById(R.id.number1), findViewById(R.id.number2),
            findViewById(R.id.number3), findViewById(R.id.number4), findViewById(R.id.number5),
            findViewById(R.id.number6), findViewById(R.id.number7), findViewById(R.id.number8),
            findViewById(R.id.number9), findViewById(R.id.decimal)
        )

        val operatorButtons = listOf<Button>(
            findViewById(R.id.addition), findViewById(R.id.substraction),
            findViewById(R.id.multiplication), findViewById(R.id.division),
            findViewById(R.id.percentage)
        )

        val clearButton = findViewById<Button>(R.id.clear)
        val equalButton = findViewById<Button>(R.id.equal)

        clearButton.setOnClickListener {
            clearDisplay()
        }

        equalButton.setOnClickListener {
            calculateResult()
        }

        for (button in numberButtons) {
            button.setOnClickListener { view ->
                val buttonText = (view as Button).text.toString()
                appendValue(buttonText)
            }
        }

        for (button in operatorButtons) {
            button.setOnClickListener { view ->
                val buttonText = (view as Button).text.toString()
                appendOperator(buttonText)
            }
        }
    }

    private fun appendValue(digit: String) {
        if(initialFlag && expression == "0"){
            expression = digit
            initialFlag = false
        } else{
            expression += digit
        }
        updateDisplay()
    }

    private fun appendOperator(operator: String) {
        expression += operator
        updateDisplay()
    }

    private fun calculateResult() {
        try {
            val result = evaluateExpression(expression)
            expression = decimalFormat.format(result)
            updateDisplay()
        } catch (e: ArithmeticException) {
            expression = "NaN"
            updateDisplay()
        } catch (e: Exception) {
            expression = "Error"
            updateDisplay()
        }
    }

    private fun evaluateExpression(expression: String): Double {
        return when {
            expression.isEmpty() -> 0.0
            expression.contains("%") -> {
                val parts = expression.split("%")
                val operand1 = parts[0].toDouble()
                operand1 / 100
            }
            expression.contains("/") -> {
                val parts = expression.split("/")
                val operand1 = parts[0].toDouble()
                val operand2 = parts[1].toDouble()
                if (operand2 == 0.0) {
                    throw ArithmeticException()
                }
                operand1 / operand2
            }
            expression.contains("*") -> {
                val parts = expression.split("*")
                val operand1 = parts[0].toDouble()
                val operand2 = parts[1].toDouble()
                operand1 * operand2
            }
            expression.contains("-") -> {
                val parts = expression.split("-")
                val operand1 = parts[0].toDouble()
                val operand2 = parts[1].toDouble()
                operand1 - operand2
            }
            expression.contains("+") -> {
                val parts = expression.split("+")
                val operand1 = parts[0].toDouble()
                val operand2 = parts[1].toDouble()
                operand1 + operand2
            }
            else -> expression.toDouble()
        }
    }

    private fun updateDisplay() {
        valueTextView.text = expression
    }

    private fun clearDisplay() {
        expression = "0"
        initialFlag = true
        updateDisplay()
    }
}
