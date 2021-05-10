package com.example.myproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myproject.App.Companion.CHANNEL_1_ID
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object {
        var locationName : String? = null
        var weatherDesc : String? = null
        var temperature : Int = 0
    }
    private lateinit var notificationManager : NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)
        createNotification()
    }

    override fun onResume() {
        super.onResume()

        thread() {
            // put json data in string
            val weatherJson : String? = getUrl("https://api.openweathermap.org/data/2.5/weather?lat=61.4898&lon=23.7735&appid=19076a0898f9475f79721bd1a75ea780&units=metric")
            println(weatherJson)
            val mapper = ObjectMapper()
            // deserialize weatherJson
            val weatherObject: WeatherJsonObject = mapper.readValue(weatherJson, WeatherJsonObject::class.java)

            locationName = weatherObject.name
            temperature = weatherObject.main.temp.toInt()

            val intent = Intent(this, WeatherNotificationActivity::class.java)
            //intent.putExtra(locationName, temperature, weatherDesc, locNameTextView, tempTextView, weatherTextView)

            println(weatherObject.name)
            println(weatherObject.main.temp)
            val currentWeather: MutableList<WeatherInfo>? = weatherObject.weather
            currentWeather?.forEach {
                println(it.main)
                weatherDesc = it.main
            }
        }
    }

    fun getUrl(url : String) : String? {
        var result : String? = ""
        val url: URL = URL(url)
        val conn = url.openConnection() as HttpURLConnection
        try {
            result = conn.inputStream.bufferedReader().use {it.readText()}
        } catch (e : Exception) {
            println(e)
        } finally {
            conn.disconnect()
        }
        return result
    }

    private fun createNotification() {
        // Open activity when notification is tapped
        val resultIntent = Intent(this, WeatherNotificationActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_strength)
                .setContentTitle("Hello")
                .setContentText("World")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build()

        notificationManager.notify(1, notification)

        //val notificationChannelId: String = NotificationUtil.createNotificationChannel(this, bigTextStyleReminderAppData)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherInfo(var main : String? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherTemp(var temp : Double = 0.0)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherJsonObject(var name : String? = null, var weather : MutableList<WeatherInfo>? = null, var main : WeatherTemp = WeatherTemp(0.0))
}