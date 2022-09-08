package com.example.filmshelper.presentation.screens.mainFragment

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.FirebaseApiService
import com.example.filmshelper.data.room.FilmsDataBase
import com.example.filmshelper.presentation.screens.mainFragment.sendFilmWorker.SendFilmWorker
import com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker.UpdateDataWorker

class MyWorkerManager(
    private val apiService: ApiService,
    private val firebaseApiService: FirebaseApiService,
    private val database: FilmsDataBase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            SendFilmWorker::class.java.name -> {
                SendFilmWorker(apiService, firebaseApiService, appContext, workerParameters)
            }
            UpdateDataWorker::class.java.name -> {
                UpdateDataWorker(appContext, workerParameters, apiService, database)
            }
            else -> {
                null
            }
        }
    }
}