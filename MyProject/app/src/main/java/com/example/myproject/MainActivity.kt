package com.example.myproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
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
    /*companion object {
        var locationName : String? = null
        var weather : String? = null
        var weatherDesc : String? = null
        var temperature : Int = 0
        var weatherId : Int = 0
        var locationLat : Double = 0.0
        var locationLong : Double = 0.0
    }*/
    private lateinit var weatherButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, BackgroundLocationService::class.java))

        weatherButton = findViewById(R.id.imageButton_weather)
    }

    fun weatherButtonClicked(button: View) {
        openWeatherActivity()
    }

    private fun openWeatherActivity() {
        val resultIntent = Intent(this, WeatherNotificationActivity::class.java)
        startActivity(resultIntent)
    }
}
