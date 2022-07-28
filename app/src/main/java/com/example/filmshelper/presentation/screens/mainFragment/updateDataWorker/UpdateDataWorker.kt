package com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.filmshelper.R
import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.room.FilmsDataBase
import javax.inject.Inject


const val channelId = "channel1"

class UpdateDataWorker @Inject constructor(
    private val context: Context,
    private val workerParams: WorkerParameters,
    private val apiService: ApiService,
    private val dataBase: FilmsDataBase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val nowShowingFilmsState = updateNowShowingFilms()
        val popularFilmsState = updatePopularFilms()
        val popularTvShowsState = updatePopularTvShows()
        if (popularFilmsState && nowShowingFilmsState && popularTvShowsState) {
            sendNotification("Data Successfully Updated")
        } else {
            sendNotification("Data Not Updated")
            return Result.retry()
        }

        return Result.success()
    }

    private suspend fun updatePopularFilms(): Boolean {
        val isSuccessful = dataBase.filmsDao().deleteAllPopularFilms()
        val popularMovies = apiService.getPopularMovies()
        return if (isSuccessful == 0) {
            false
        } else {
            popularMovies.results.forEach {
                dataBase.filmsDao().addOrUpdatePopularFilms(it)
            }

            val list = dataBase.filmsDao().readAllPopularFilms()

            list.isNotEmpty()

        }

    }

    private suspend fun updateNowShowingFilms(): Boolean {
        val isSuccessful = dataBase.filmsDao().deleteAllNowShowingFilms()
        val nowShowingFilms = apiService.getMoviesInTheaters()
        return if (isSuccessful == 0) {
            false
        } else {
            nowShowingFilms.results.forEach {
                dataBase.filmsDao().addOrUpdateNowShowingFilms(it)
            }

            val list = dataBase.filmsDao().readAllNowShowingFilms()

            list.isNotEmpty()

        }

    }

    private suspend fun updatePopularTvShows(): Boolean {
        val isSuccessful = dataBase.filmsDao().deleteAllPopularTvShows()
        val popularTvShows = apiService.getPopularTvShows()
        return if (isSuccessful == 0) {
            false
        } else {
            popularTvShows.results.forEach {
                dataBase.filmsDao().addOrUpdatePopularTvShows(it)
            }

            val list = dataBase.filmsDao().readAllPopularTvShows()

            list.isNotEmpty()

        }

    }

    private fun sendNotification(data: String) {

        val CHANNEL_ID = "channel_name"

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Data Update")
                .setContentText(data)
                .setAutoCancel(false)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Channel Name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(
            idWork,
            notificationBuilder.build()
        )


    }

    class Factory @Inject constructor(
        val apiService: ApiService,
        val dataBase: FilmsDataBase
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return UpdateDataWorker(appContext, params, apiService, dataBase)
        }

    }

    interface ChildWorkerFactory {
        fun create(appContext: Context, params: WorkerParameters): CoroutineWorker
    }


    companion object {
        var idWork = 0
    }

}