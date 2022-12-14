package com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class UpdateDataWorkerFactory @Inject constructor(
    private val workerFactories: MutableMap<Class<out ListenableWorker>, Provider<UpdateDataWorker.ChildWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {

        var listenableWorker: ListenableWorker? = null
        try {
            var provider: Provider<out UpdateDataWorker.ChildWorkerFactory>? = null
            for ((key, value) in workerFactories) {
                if (Class.forName(workerClassName).isAssignableFrom(key)) {
                    provider = value
                    break
                }
            }
            if (provider == null) {
                throw IllegalArgumentException("unknown model class $workerClassName")
            }
            listenableWorker = provider.get().create(appContext, workerParameters)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return listenableWorker


    }
}