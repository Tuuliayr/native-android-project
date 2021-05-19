package com.example.myproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myproject.Constants.CHANNEL_1_ID
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.vmadalin.easypermissions.EasyPermissions
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object {
        var locationName : String? = null
        var weather : String? = null
        var weatherDesc : String? = null
        var temperature : Int = 0
        var weatherId : Int = 0
        var locationLat : Double = 0.0
        var locationLong : Double = 0.0
    }
    private lateinit var notificationManager : NotificationManagerCompat
    private lateinit var weatherButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherButton = findViewById(R.id.imageButton_weather)

        notificationManager = NotificationManagerCompat.from(this)
        createNotification()
    }

    override fun onResume() {
        super.onResume()

        thread() {
            // put json data in string
            val weatherJson : String? = getUrl("https://api.openweathermap.org/data/2.5/weather?lat=$locationLat&lon=$locationLong&appid=19076a0898f9475f79721bd1a75ea780&units=metric")
            println(weatherJson)
            val mapper = ObjectMapper()
            // deserialize weatherJson
            val weatherObject: WeatherJsonObject = mapper.readValue(weatherJson, WeatherJsonObject::class.java)

            locationName = weatherObject.name
            temperature = weatherObject.main.temp.toInt()

            println(weatherObject.name)
            println(weatherObject.main.temp)
            val currentWeather: MutableList<WeatherInfo>? = weatherObject.weather
            currentWeather?.forEach {
                println(it.main)
                weather = it.main
                weatherDesc = it.description
                weatherId = it.id
            }
        }
    }

    fun weatherButtonClicked(button: View) {
        openWeatherActivity()
    }

    private fun openWeatherActivity() {
        val resultIntent = Intent(this, WeatherNotificationActivity::class.java)
        startActivity(resultIntent)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun createNotification() {
        // Open activity when notification is tapped
        val resultIntent = Intent(this, WeatherNotificationActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_strength)
                .setContentTitle("Current Weather")
                .setContentText("weather")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build()

        notificationManager.notify(1, notification)

        //val notificationChannelId: String = NotificationUtil.createNotificationChannel(this, bigTextStyleReminderAppData)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherInfo(var id : Int = 0, var main : String? = null, var description : String? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherTemp(var temp : Double = 0.0)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherJsonObject(var name : String? = null, var weather : MutableList<WeatherInfo>? = null, var main : WeatherTemp = WeatherTemp(0.0))
}
