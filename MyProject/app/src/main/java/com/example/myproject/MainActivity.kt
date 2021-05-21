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
    private lateinit var exerciseTextView: TextView
    private lateinit var weatherButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //startService(Intent(this, BackgroundLocationService::class.java))

        //val startIntent = Intent(this, BackgroundLocationService::class.java)
        //ContextCompat.startForegroundService(this, startIntent)
        //val service = BackgroundLocationService()
        val serviceIntent = Intent(this, BackgroundLocationService::class.java)
        if (!isMyServiceRunning(BackgroundLocationService::class.java)) {
            startService(serviceIntent);
        }
        exerciseTextView = findViewById(R.id.textView_exercise)
        weatherButton = findViewById(R.id.imageButton_weather)
    }

    override fun onDestroy() {
        //stopService(mServiceIntent);
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
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

    fun weatherButtonClicked(button: View) {
        openWeatherActivity()
    }

    fun exerciseButtonClicked(button: View) {
        var exercise = ""
        val random = (1..11).random()
        val allExercises = mapOf(1 to "10 squats", 2 to "20 squats", 3 to "40 squats",
                4 to "Elbow plank 1 min", 5 to "Cat cow stretch 1 min", 6 to "Cobra stretch 30 sec + child's pose 30 sec",
                7 to "Hamstring stretch 30 sec", 8 to "Standing quad stretch 30 sec", 9 to "Number 4 sit 30 sec",
                10 to "Hip flexor stretch 30 sec", 11 to "Spinal twist 15-30 sec")
        exercise = allExercises[random].toString()

        exerciseTextView.text = exercise
    }

    private fun openWeatherActivity() {
        val resultIntent = Intent(this, WeatherNotificationActivity::class.java)
        startActivity(resultIntent)
    }
}
