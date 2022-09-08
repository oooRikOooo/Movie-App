package com.example.filmshelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val serviceIntent = Intent(context, BootService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}