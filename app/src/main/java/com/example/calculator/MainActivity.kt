package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.Stack
import kotlin.math.exp

class MainActivity : AppCompatActivity() {


    private lateinit var valueTextView: TextView
    private var currentValue: String = "0"
    private var expressionStack = Stack<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        valueTextView = findViewById<TextView>(R.id.value)

        expressionStack.push(0)

        val numberButtons = listOf(
            R.id.number0, R.id.number01, R.id.number02, R.id.number03,
            R.id.number04, R.id.number05, R.id.number06, R.id.number07,
            R.id.number08, R.id.number09, R.id.decimal
        )
        numberButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                var topValue:Any = expressionStack.peek()
                var newNumberString:String = (it as Button).text.toString()

                if(topValue == "%"){
                    try {
                        val result = calculateValue()
                        currentValue = result.toString()
                        valueTextView.text = currentValue
                    } catch (e: Exception) {
                        currentValue = "Error: ${e.javaClass.simpleName}"
                        valueTextView.text = currentValue
                    }
                }

                if(topValue is String && expressionStack.size == 3){
                    try {
                        val result = calculateValue()
                        currentValue = result.toString()
                        valueTextView.text = currentValue
                    } catch (e: Exception) {
                        currentValue = "Error: ${e.javaClass.simpleName}"
                        valueTextView.text = currentValue
                    }
                }

                if(topValue is Int || topValue is Double || topValue == "."){
                    while(expressionStack.isNotEmpty()){
                        if(topValue is String && topValue !== "."){
                            break
                        }
                        newNumberString = topValue.toString() + newNumberString
                        expressionStack.pop()
                        topValue = expressionStack.peek()
                    }
                }

                val newNumber = newNumberString.toDouble()
                expressionStack.push(newNumber)

                appendToValue(newNumberString)
            }
        }

        val operatorButtons = listOf(
            R.id.addition, R.id.substraction,
            R.id.multiplication, R.id.division, R.id.percentage
        )
        operatorButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {

                if(expressionStack.peek() == "%"){
                    try {
                        val result = calculateValue()
                        currentValue = result.toString()
                        valueTextView.text = currentValue
                    } catch (e: Exception) {
                        currentValue = "Error: ${e.javaClass.simpleName}"
                        valueTextView.text = currentValue
                    }
                }
                else if(expressionStack.size == 3){
                    try {
                        val result = calculateValue()
                        currentValue = result.toString()
                        valueTextView.text = currentValue
                    } catch (e: Exception) {
                        currentValue = "Error: ${e.javaClass.simpleName}"
                        valueTextView.text = currentValue
                    }
                }
                expressionStack.push((it as Button).text.toString())
                performOperation((it).text.toString())
            }
        }

        findViewById<Button>(R.id.clear).setOnClickListener{
            clearValue()
        }

        findViewById<Button>(R.id.equal).setOnClickListener{
            try {
                val result = calculateValue()
                currentValue = result.toString()
                valueTextView.text = currentValue
            } catch (e: Exception) {
                currentValue = "Error: ${e.javaClass.simpleName}"
                valueTextView.text = currentValue
            }
        }
    }

    private fun appendToValue(text: String) {
        currentValue = if(currentValue == "0") {
            text
        } else {
            currentValue + text
        }
        valueTextView.text = currentValue
    }

    private  fun performOperation(operator: String) {
        currentValue += operator
        valueTextView.text = currentValue
    }

    private fun clearValue(){

        //to clear stack
        while(expressionStack.isNotEmpty()){
            expressionStack.pop()
        }

        //to change the value in the TextView
        currentValue = "0"
        valueTextView.text = currentValue
    }

    private fun calculateValue(): Double {

        for (index in 1 until expressionStack.size) {
            if((expressionStack[index-1] != '+' || expressionStack[index-1] != '-' ||expressionStack[index-1] != '/' ||expressionStack[index-1] != '*') &&
                (expressionStack[index] != '+' || expressionStack[index] != '-' ||expressionStack[index] != '/' ||expressionStack[index] != '*')){
                return throw IllegalArgumentException("Invalid expression")
            }
        }

        if(expressionStack.size == 3){
            val operand2 = expressionStack.pop()
            val operator = expressionStack.pop()
            val operand1 = expressionStack.pop()

            return when(operator) {
                "+" -> {
                    val result = operand1.toString().toDouble() + operand2.toString().toDouble()
                    expressionStack.push(result)
                    result
                } "-" -> {
                    val result = operand1.toString().toDouble() - operand2.toString().toDouble()
                    expressionStack.push(result)
                    result
                } "*" -> {
                    val result = operand1.toString().toDouble() * operand2.toString().toDouble()
                    expressionStack.push(result)
                    result
                } "/" -> {
                    if(operand2.toString().toDouble() == 0.0){
                        throw ArithmeticException("Dividing By Zero")
                    }
                    else{
                        val result = operand1.toString().toDouble() / operand2.toString().toDouble()
                        expressionStack.push(result)
                        result
                    }

                } else -> throw UnsupportedOperationException("Unsupported Operator")

            }
        }

        else if ( expressionStack.size == 2){
            val operator = expressionStack.pop()
            val operand = expressionStack.pop()

            return when (operator) {
                "%" -> operand.toString().toDouble() / 100
                else -> throw UnsupportedOperationException("Unsupported Operator")
            }
        }
        return throw IllegalArgumentException("Invalid Operation")
    }

}