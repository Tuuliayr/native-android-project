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
    /*companion object {
        const val CHANNEL_1_ID: String = "channel1"
        const val CHANNEL_2_ID: String = "channel2"
    }*/
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    // Create notification channels for android versions greater than than oreo (api level 26)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // channel 1
            val channel1 =
                    NotificationChannel(CHANNEL_1_ID, "channel1",
                            NotificationManager.IMPORTANCE_HIGH)
            channel1.description = "This is channel 1"

            // channel 2
            val channel2 =
                    NotificationChannel(CHANNEL_2_ID, "channel2",
                            NotificationManager.IMPORTANCE_NONE)
            channel2.description = "This is channel 2"

            // register them
            val notificationManager = getSystemService(NotificationManager::class.java)
            //val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }
}