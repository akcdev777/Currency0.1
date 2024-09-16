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
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI

class MainActivity : AppCompatActivity() {
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

        val convertButton = findViewById<Button>(R.id.convertButton)
        convertButton.setOnClickListener { view ->
            try {
                val rates = fetchExchangeRates()
                if (rates.isNotEmpty()) {
                    when (lastEdited) {
                        "GBP" -> {
                            val pounds = poundText.text.toString().toFloat()
                            dollarText.setText((Math.round(pounds * rates["GBP_USD"]!! * 100) / 100f).toString())
                            euroText.setText((Math.round(pounds * rates["GBP_EUR"]!! * 100) / 100f).toString())
                        }
                        "USD" -> {
                            val dollars = dollarText.text.toString().toFloat()
                            poundText.setText((Math.round(dollars * rates["USD_GBP"]!! * 100) / 100f).toString())
                            euroText.setText((Math.round(dollars * rates["USD_EUR"]!! * 100) / 100f).toString())
                        }
                        "EUR" -> {
                            val euros = euroText.text.toString().toFloat()
                            poundText.setText((Math.round(euros * rates["EUR_GBP"]!! * 100) / 100f).toString())
                            dollarText.setText((Math.round(euros * rates["EUR_USD"]!! * 100) / 100f).toString())
                        }
                    }
                } else {
                    Toast.makeText(view.context, "Unable to fetch exchange rates", Toast.LENGTH_SHORT).show()
                }
            } catch (exception: Exception) {
                Toast.makeText(view.context, "Invalid data, try again", Toast.LENGTH_SHORT).show()
            }
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

    private fun fetchExchangeRates(): Map<String, Float> {
        val apiKey = "YOUR_API_KEY_HERE"
        val currencies = listOf("GBP", "USD", "EUR")
        val rates = mutableMapOf<String, Float>()

        try {
            for (i in currencies.indices) {
                for (j in i + 1 until currencies.size) {
                    val fromCurrency = currencies[i]
                    val toCurrency = currencies[j]
                    val apiUrl = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=$fromCurrency&to_currency=$toCurrency&apikey=$apiKey"

                    val url = URI.create(apiUrl).toURL()
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val reader = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }

                        reader.close()

                        // Parse JSON to get exchange rate
                        val jsonResponse = JSONObject(response.toString())
                        val exchangeRate = jsonResponse
                            .getJSONObject("Realtime Currency Exchange Rate")
                            .getString("5. Exchange Rate").toFloat()

                        rates["${fromCurrency}_${toCurrency}"] = exchangeRate
                        rates["${toCurrency}_${fromCurrency}"] = 1 / exchangeRate
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rates
    }


}