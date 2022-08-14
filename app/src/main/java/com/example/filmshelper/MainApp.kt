package com.example.filmshelper

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.FirebaseApiService
import com.example.filmshelper.data.room.FilmsDataBase
import com.example.filmshelper.di.AppComponent
import com.example.filmshelper.di.DaggerAppComponent
import com.example.filmshelper.presentation.screens.mainFragment.MyWorkerManager
import javax.inject.Inject

class MainApp : Application(), Configuration.Provider {

    /*@Inject
    lateinit var updateDataWorkerFactory: UpdateDataWorkerFactory

    @Inject
    lateinit var sendFilmWorkerFactory: SendFilmWorkerFactory*/

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var firebaseApiService: FirebaseApiService

    @Inject
    lateinit var dataBase: FilmsDataBase


    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)

        appComponent.inject(this)

        /*val workManagerConfig = Configuration.Builder()
            .setWorkerFactory(updateDataWorkerFactory)
            .setWorkerFactory(sendFilmWorkerFactory)
            .build()

        WorkManager.initialize(this, workManagerConfig)*/
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val myWorkerFactory = DelegatingWorkerFactory()

        /*myWorkerFactory.addFactory(updateDataWorkerFactory)
        myWorkerFactory.addFactory(sendFilmWorkerFactory)*/
        myWorkerFactory.addFactory(MyWorkerManager(apiService, firebaseApiService, dataBase))

        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(myWorkerFactory)
            .build()

    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.appComponent
    }


