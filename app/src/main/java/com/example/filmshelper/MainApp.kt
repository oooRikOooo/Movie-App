package com.example.filmshelper

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.filmshelper.di.AppComponent
import com.example.filmshelper.di.DaggerAppComponent
import com.example.filmshelper.presentation.screens.mainFragment.sendFilmWorker.SendFilmWorkerFactory
import com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker.UpdateDataWorkerFactory
import javax.inject.Inject

class MainApp : Application() {

    @Inject
    lateinit var updateDataWorkerFactory: UpdateDataWorkerFactory

    @Inject
    lateinit var sendFilmWorkerFactory: SendFilmWorkerFactory

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)

        appComponent.inject(this)

        val workManagerConfig = Configuration.Builder()
            .setWorkerFactory(updateDataWorkerFactory)
            .setWorkerFactory(sendFilmWorkerFactory)
            .build()

        WorkManager.initialize(this, workManagerConfig)
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.appComponent
    }


