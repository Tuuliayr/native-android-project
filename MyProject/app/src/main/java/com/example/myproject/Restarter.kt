package com.example.myproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.os.Build
import android.util.Log

import android.widget.Toast

/*
* Restart service after app is killed
*
 */
class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i("Broadcast Listened", "Service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()

        // In Android versions above Oreo startService would terminate service once the app is killed,
        // so instead, a foreground service is started with a continuous notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, BackgroundLocationService::class.java))
        } else {
            context.startService(Intent(context, BackgroundLocationService::class.java))
        }
    }
}