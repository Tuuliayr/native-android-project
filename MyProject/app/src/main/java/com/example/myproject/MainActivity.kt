package com.example.myproject

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


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

        //startService(Intent(this, BackgroundLocationService::class.java))

        //val startIntent = Intent(this, BackgroundLocationService::class.java)
        //ContextCompat.startForegroundService(this, startIntent)
        val service = BackgroundLocationService()
        val serviceIntent = Intent(this, BackgroundLocationService::class.java)
        if (!isMyServiceRunning(BackgroundLocationService::class.java)) {
            startService(serviceIntent);
        }

        weatherButton = findViewById(R.id.imageButton_weather)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    override fun onDestroy() {
        //stopService(mServiceIntent);
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    fun weatherButtonClicked(button: View) {
        openWeatherActivity()
    }

    private fun openWeatherActivity() {
        val resultIntent = Intent(this, WeatherNotificationActivity::class.java)
        startActivity(resultIntent)
    }
}
