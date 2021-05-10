package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myproject.MainActivity.Companion.locationName
import com.example.myproject.MainActivity.Companion.temperature
import com.example.myproject.MainActivity.Companion.weatherDesc

class WeatherNotificationActivity : AppCompatActivity() {
    lateinit var locNameTextView : TextView
    lateinit var weatherTextView : TextView
    lateinit var tempTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_notification)

        locNameTextView = findViewById<TextView>(R.id.locName)
        locNameTextView.setText(locationName)
        weatherTextView = findViewById<TextView>(R.id.weather)
        weatherTextView.setText(weatherDesc)
        tempTextView = findViewById<TextView>(R.id.temp)
        tempTextView.setText(getString(R.string.temp_celsius, temperature))
    }
}