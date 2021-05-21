package com.example.myproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myproject.Constants.CHANNEL_1_ID
import com.example.myproject.Constants.CHANNEL_2_ID

class App : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    // Create notification channels for android versions greater than than oreo (api level 26)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // channel 1 for weather notification
            val channel1 =
                    NotificationChannel(CHANNEL_1_ID, "Clear weather",
                            NotificationManager.IMPORTANCE_HIGH)
            channel1.description = "This is channel 1"

            // channel 2 for foreground's continuous notification
            val channel2 =
                    NotificationChannel(CHANNEL_2_ID, "My Project",
                            NotificationManager.IMPORTANCE_NONE)
            channel2.description = "This is channel 2"

            // register them
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }
}