package com.example.currency01

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val GBPtoEUR = 1.1856
    private val GBPtoUSD = 1.3122
    private val EURtoGBP = 0.84
    private val EURtoUSD = 1.11
    private val USDToEUR = 0.9
    private val USDToGBP = 0.76

    lateinit var poundText: EditText
    lateinit var dollarText: EditText
    lateinit var euroText: EditText

    private var lastEdited: String = "GBP"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        poundText = findViewById(R.id.poundsValue) as EditText
        dollarText = findViewById(R.id.dollarValue ) as EditText
        euroText = findViewById(R.id.euroValue ) as EditText

        poundText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lastEdited = "GBP"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dollarText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lastEdited = "USD"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        euroText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lastEdited = "EUR"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })




        val convertButton = findViewById<View>(R.id.convertButton) as Button
        convertButton.setOnClickListener { view ->
            try{
                when (lastEdited) { //convert currencies based on last modified field
                    "GBP" -> {
                        val pounds = poundText.text.toString().toFloat()
                        dollarText.setText((Math.round(pounds * GBPtoUSD * 100) / 100f).toString())
                        euroText.setText((Math.round(pounds * GBPtoEUR * 100) / 100f).toString())
                    }
                    "USD" -> {
                        val dollars = dollarText.text.toString().toFloat()
                        poundText.setText((Math.round(dollars * USDToGBP * 100) / 100f).toString())
                        euroText.setText((Math.round(dollars * USDToEUR * 100) / 100f).toString())
                    }
                    "EUR" -> {
                        val euros = euroText.text.toString().toFloat()
                        poundText.setText((Math.round(euros * EURtoGBP * 100) / 100f).toString())
                        dollarText.setText((Math.round(euros * EURtoUSD * 100) / 100f).toString())
                    }
                }
            }catch (exception: Exception){
                Toast.makeText(view.context, "Invalid data try again", Toast.LENGTH_SHORT).show()
            }//end exception
        }//end button

        val clearButton = findViewById<View>(R.id.clearButton) as Button
        clearButton.setOnClickListener { view ->
            try{
                poundText.setText("")
                dollarText.setText("")
                euroText.setText("")

            }catch(exception: Exception){
                Toast.makeText(view.context, "Something went wrong", Toast.LENGTH_SHORT).show()

            }//end exception
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
    }
}