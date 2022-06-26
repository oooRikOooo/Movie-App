package com.example.filmshelper

import android.app.Application
import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.repository.MainScreenRepositoryImpl
import com.example.filmshelper.domain.repository.MainScreenRepository
import com.example.filmshelper.presentation.screens.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component(modules = [AppModule::class])
interface AppComponent {

    /*fun inject(application: MainApp)*/
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: LocationFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: FilmDetailsFragment)

}

@Module(includes = [NetworkModule::class, AppBindModule::class])
class AppModule

@Module
class NetworkModule{

    @Provides
    fun provideApiService(): ApiService{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://imdb-api.com/en/API/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

@Module
interface AppBindModule {

    @Suppress("FunctionName")
    @Binds
    fun bindMainScreenRepositoryImpl_to_MainScreenRepository(
        mainScreenRepositoryImpl: MainScreenRepositoryImpl
    ): MainScreenRepository
}
