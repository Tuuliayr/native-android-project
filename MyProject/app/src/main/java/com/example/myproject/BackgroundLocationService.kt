package com.example.myproject

import android.app.Service
import android.content.Intent
import android.nfc.Tag
import android.os.IBinder
import android.util.Log
import kotlin.concurrent.thread

class BackgroundLocationService : Service() {
    private var loop = true
    private val tag = "location service"

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(tag, "Service started")

        val t = thread() {
            var i = 1
            while (loop) {
                Log.d(tag, Thread.currentThread().name)
                sendBroadcast(Intent("location"))
                Thread.sleep(5000)
                i++
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        loop = false
        Log.d(tag, "Service stopped")
    }

}