package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.MainActivity.Companion.locationName
import com.example.myproject.MainActivity.Companion.temperature
import com.example.myproject.MainActivity.Companion.weather
import com.example.myproject.MainActivity.Companion.weatherDesc
import com.example.myproject.MainActivity.Companion.weatherId


class WeatherNotificationActivity : AppCompatActivity() {
    lateinit var locNameTextView : TextView
    lateinit var weatherTextView : TextView
    lateinit var infoTextView: TextView
    lateinit var tempTextView : TextView
    lateinit var weatherImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_notification)

        locNameTextView = findViewById(R.id.textView_locName)
        locNameTextView.setText(locationName)
        weatherTextView = findViewById(R.id.textView_weather)
        weatherTextView.setText(weather)
        infoTextView = findViewById(R.id.textView_weatherInfo)
        infoTextView.setText(weatherDesc)
        tempTextView = findViewById(R.id.textView_temp)
        tempTextView.setText(getString(R.string.temp_celsius, temperature))

        weatherImageView = findViewById(R.id.imageView_weatherIcon)
        var icon : String = getWeatherIcon(weatherId)
        weatherImageView.setImageResource(resources.getIdentifier(icon, "drawable", packageName));
    }

    fun backButtonClicked(button: View) {
        openMainActivity()
    }

    private fun openMainActivity() {
        val resultIntent = Intent(this, MainActivity::class.java)
        startActivity(resultIntent)
    }

    // Find icon by id and return the name
    private fun getWeatherIcon(id : Int): String {
        val idString = id.toString()
        var icon = ""
        val idFirstChar = idString.first()
        val idSecondChar = idString[1]
        // put image names in a list
        val weatherIcons = listOf("ic_01d", "ic_02d", "ic_03d", "ic_04d", "ic_09d", "ic_10d",
                                    "ic_11d", "ic_13d", "ic_50d")
        when (idFirstChar) {
            // Thunderstorm
            '2' -> icon = weatherIcons[6]
            // Drizzle
            '3' -> icon = weatherIcons[4]
            // Rain
            '5' -> icon = when(idSecondChar) {
                '0' -> weatherIcons[5]
                '2', '3' -> weatherIcons[4]
                else -> weatherIcons[7]
            }
            // Snow
            '6' -> icon = weatherIcons[7]
            // Atmosphere
            '7' -> weatherIcons[8]
            // Clear and clouds
            '8' -> icon = when(id) {
                800 -> weatherIcons[0]
                801 -> weatherIcons[1]
                802 -> weatherIcons[2]
                else -> weatherIcons[3]
            }
            else -> {
                print("No icon found with id $id")
            }
        }
        return icon
    }
}