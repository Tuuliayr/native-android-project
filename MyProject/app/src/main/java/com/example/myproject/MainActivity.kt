package com.example.myproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myproject.App.Companion.CHANNEL_1_ID

class MainActivity : AppCompatActivity() {
    private lateinit var notificationManager : NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)
        createNotification()
    }

    fun getURL() {

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
}