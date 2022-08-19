package com.example.filmshelper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.FirebaseApiService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


const val channelId = "channel1"

class BootService : Service() {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var firebaseApiService: FirebaseApiService

    private var tokenValue: String? = null


    override fun onCreate() {
        FirebaseApp.initializeApp(applicationContext)
        FirebaseMessaging.getInstance().subscribeToTopic("Message")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result

            tokenValue = token

        })

        appComponent.inject(this)




        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        isRunning = true

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "First notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Top Tv Serial Now")
            .setContentText("Stranger Things ")
            .setAutoCancel(true)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .build()

        notificationManager.notify(id++, notification)

        startForeground(1, notification)


        startCoroutine()

        return START_STICKY
    }

    override fun onDestroy() {
        isRunning = false
        isFirst = true
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun startCoroutine() {
        CoroutineScope(Dispatchers.IO).launch {
            val manager: NotificationManager =
                this@BootService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val mChannel =
                    NotificationChannel(channelId, "riko", NotificationManager.IMPORTANCE_NONE)
                mChannel.description = "This is my channel"
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(false)
                manager.createNotificationChannel(mChannel)
            }

            val film = apiService.getPopularMovies(count = 100).results.random()

            val data = buildData(film.title, tokenValue!!)

            getMessage(data)

        }

    }

    private fun getMessage(data: JsonObject) = CoroutineScope(Dispatchers.IO).launch {
        firebaseApiService.getMessage(data)
    }

    private fun buildData(value: String, newToken: String): JsonObject {

        val jsonObject = JsonObject()
        jsonObject.addProperty("to", newToken)
        val data = JsonObject()
        data.addProperty("title", "Recommended to watch")
        data.addProperty("body", value)
        jsonObject.add("notification", data)

        return jsonObject
    }


    companion object {
        var isRunning = false
        var id = 0
        var isFirst = true
    }
}