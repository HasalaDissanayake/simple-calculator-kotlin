package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var displayTextView: TextView
    private var input: String = ""
    private var operator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.value)


        input = ""
        operator = ""


        for (i in 0..9) {
            val buttonId = resources.getIdentifier("number$i", "id", packageName)
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener {
                input += i.toString()
                displayTextView.text = input
            }
        }

        val decimalButton = findViewById<Button>(R.id.decimal)
        decimalButton.setOnClickListener {
            if (!input.contains(".")) {
                input += "."
                displayTextView.text = input
            }
        }

        findViewById<Button>(R.id.addition).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.substraction).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.multiplication).setOnClickListener { setOperator("*") }
        findViewById<Button>(R.id.division).setOnClickListener { setOperator("/") }
        findViewById<Button>(R.id.percentage).setOnClickListener { setOperator("%") }

        findViewById<Button>(R.id.equal).setOnClickListener { calculateResult() }

        findViewById<Button>(R.id.clear).setOnClickListener { clearCalculator() }
    }

    private fun setOperator(operator: String) {
        this.operator = operator
        input += operator
        displayTextView.text = input
    }

    private fun clearCalculator() {
        input = ""
        operator = ""
        displayTextView.text = ""
    }

    private fun calculateResult() {
        if (operator.isEmpty() || input.endsWith(operator)) {
            displayTextView.text = input
            return
        }

        val operands = input.split("\\\\" + operator).toTypedArray()

        if (operands.size != 2) {
            displayTextView.text = "Error"
            return
        }

        val operand1 = operands[0].toDouble()
        val operand2 = operands[1].toDouble()

        val result: Double = when (operator) {
            "+" -> operand1 + operand2
            "-" -> operand1 - operand2
            "*" -> operand1 * operand2
            "/" -> {
                if (operand2 == 0.0) {
                    displayTextView.text = "Error"
                    return
                }
                operand1 / operand2
            }
            "%" -> operand1 * (operand2 / 100)
            else -> {
                displayTextView.text = "Error"
                return
            }
        }

        displayTextView.text = result.toString()
        input = result.toString()
        operator = ""
    }
}

