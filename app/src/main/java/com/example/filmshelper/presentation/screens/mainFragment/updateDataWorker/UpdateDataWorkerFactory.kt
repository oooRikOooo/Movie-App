package com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class UpdateDataWorkerFactory @Inject constructor(
    //private val updateDataWorkerFactory: UpdateDataWorker.Factory
    private val workerFactories: MutableMap<Class<out ListenableWorker>, Provider<UpdateDataWorker.CustomWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        /* return when (workerClassName) {
             UpdateDataWorker::class.java.name ->
                 updateDataWorkerFactory.create(appContext, workerParameters)
             else -> null
         }*/
        var listenableWorker: ListenableWorker? = null
        try {
            var provider: Provider<out UpdateDataWorker.CustomWorkerFactory>? = null
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

        /*val foundEntry =
            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
            ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
        return factoryProvider.get().create(appContext, workerParameters)*/
    }
}