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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        /*val channelName = "Background Service"
        val chan = NotificationChannel(
            CHANNEL_2_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE*/
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
                sendBroadcast(Intent("location"))
                //Thread.sleep(5000)
                TimeUnit.HOURS.sleep(2)
            }
        }
        return START_STICKY
    }
}