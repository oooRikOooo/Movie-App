package com.example.filmshelper.presentation.screens.mainFragment.sendFilmWorker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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

class SendFilmWorker @Inject constructor(
    private val apiService: ApiService,
    private val firebaseApiService: FirebaseApiService,
    private val appContext: Context,
    private val params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    private var tokenValue: String? = null

    override suspend fun doWork(): Result {
        FirebaseApp.initializeApp(appContext)
        FirebaseMessaging.getInstance().subscribeToTopic("Message")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result

            tokenValue = token

            startCoroutine()

        })

        return Result.success()

    }

    private fun startCoroutine() {
        CoroutineScope(Dispatchers.IO).launch {
            val manager: NotificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val mChannel =
                    NotificationChannel(
                        "Send Film Channel",
                        "riko",
                        NotificationManager.IMPORTANCE_NONE
                    )
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

    class Factory @Inject constructor(
        private val apiService: ApiService,
        private val firebaseApiService: FirebaseApiService
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return SendFilmWorker(apiService, firebaseApiService, appContext, params)
        }

    }

    interface ChildWorkerFactory {
        fun create(appContext: Context, params: WorkerParameters): CoroutineWorker
    }


    companion object {
        var idWork = 10
    }
}