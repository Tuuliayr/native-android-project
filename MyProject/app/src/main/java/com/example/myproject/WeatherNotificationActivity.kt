package com.example.myproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.MainActivity.Companion.locationName
import com.example.myproject.MainActivity.Companion.temperature
import com.example.myproject.MainActivity.Companion.weatherDesc


class WeatherNotificationActivity : AppCompatActivity() {
    lateinit var locNameTextView : TextView
    lateinit var weatherTextView : TextView
    lateinit var tempTextView : TextView
    lateinit var weatherImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_notification)

        locNameTextView = findViewById<TextView>(R.id.locName)
        locNameTextView.setText(locationName)
        weatherTextView = findViewById<TextView>(R.id.weather)
        weatherTextView.setText(weatherDesc)
        tempTextView = findViewById<TextView>(R.id.temp)
        tempTextView.setText(getString(R.string.temp_celsius, temperature))

        weatherImageView = findViewById(R.id.weatherIcon)
        weatherImageView.setImageResource(R.drawable.ic_02d)
    }
}