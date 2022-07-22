package com.example.filmshelper

import android.content.Context
import androidx.room.Room
import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl
import com.example.filmshelper.data.dataSources.mainScreen.RemoteDataSourceImpl
import com.example.filmshelper.data.repository.MainScreenRepositoryImpl
import com.example.filmshelper.data.repository.ProfileRepositoryImpl
import com.example.filmshelper.data.room.FilmsDataBase
import com.example.filmshelper.domain.dataSources.FilmsDataSource
import com.example.filmshelper.domain.repository.MainScreenRepository
import com.example.filmshelper.domain.repository.ProfileRepository
import com.example.filmshelper.presentation.screens.FilmDetailsFragment
import com.example.filmshelper.presentation.screens.LocationFragment
import com.example.filmshelper.presentation.screens.SearchFragment
import com.example.filmshelper.presentation.screens.mainFragment.MainFragment
import com.example.filmshelper.presentation.screens.profileFragments.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: LocationFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: FilmDetailsFragment)
    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: FavouritesFilmsFragment)
    fun inject(fragment: ProfileSignInFragment)
    fun inject(fragment: ProfileSignUpFragment)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

}

@Module(includes = [NetworkModule::class, AppBindModule::class, LocaleModule::class])
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
class LocaleModule{

    @Provides
    fun provideMovieDataBase(context: Context): FilmsDataBase{
        return Room.databaseBuilder(context, FilmsDataBase::class.java, "films_database")
            .fallbackToDestructiveMigration().build()
    }
}

@Module
interface AppBindModule {

    @Suppress("FunctionName")
    @Binds
    fun bindMainScreenRepositoryImpl_to_MainScreenRepository(
        mainScreenRepositoryImpl: MainScreenRepositoryImpl
    ): MainScreenRepository

    @Suppress("FunctionName")
    @Binds
    fun bindProfileRepositoryImpl_to_ProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Suppress("FunctionName")
    @Binds
    fun bindRemoteDataSourceImpl_toDataSource(
        remoteDataSourceImpl : RemoteDataSourceImpl
    ) : FilmsDataSource

    @Suppress("FunctionName")
    @Binds
    fun bindLocaleDataSourceImpl_toDataSource(
        localeDataSourceImpl : LocaleDataSourceImpl
    ) : FilmsDataSource
}
