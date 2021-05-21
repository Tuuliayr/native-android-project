package com.example.myproject

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myproject.Constants.CHANNEL_2_ID
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class BackgroundLocationService : Service() {
    private var loop = true
    private val tag = "location service"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1,
            Notification()
        )
    }

    // Continuous notification
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_2_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(tag, "Service started")

        val t = thread() {
            while (loop) {
                Log.d(tag, Thread.currentThread().name)
                // Send broadcast if time is between 9am and 8pm, do again in 5 hours
                // If time is not right try again in 2 hours
                if (isItTime()) {
                    sendBroadcast(Intent("location"))
                    TimeUnit.HOURS.sleep(5)
                } else {
                    TimeUnit.HOURS.sleep(2)
                }
            }
        }
        return START_STICKY
    }

    /*
    * Checks current hour, returns false if the time is not between 9am and 8pm
     */
    private fun isItTime() : Boolean {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        println(currentHour)
        return currentHour in 9..19
    }
}