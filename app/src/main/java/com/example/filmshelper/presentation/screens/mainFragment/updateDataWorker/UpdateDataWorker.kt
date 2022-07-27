package com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.filmshelper.R
import com.example.filmshelper.data.ApiService
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject


const val channelId = "channel1"

class UpdateDataWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val apiService: ApiService
) : CoroutineWorker(context, workerParams) {

    /*@Inject
    lateinit var apiService: ApiService*/

    override suspend fun doWork(): Result {

        val data = apiService.getPopularMovies()

        if (data.errorMessage.toString().isEmpty()) {
            sendNotification(data.results.size.toString())
        } else return Result.retry()

        Log.d("riko", "working")

        return Result.success(Data.Builder().putString("key", "key").build())
    }

    private fun sendNotification(data: String) {

        val CHANNEL_ID = "channel_name" // The id of the channel.

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("dawda")
                .setContentText("Size: $data")
                .setAutoCancel(false)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Channel Name" // The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(
            idWork,
            notificationBuilder.build()
        )


    }

    @AssistedInject.Factory
    interface Factory : CustomWorkerFactory

    interface CustomWorkerFactory {
        fun create(context: Context, workerParams: WorkerParameters): ListenableWorker
    }

    companion object {
        var idWork = 0
    }

}