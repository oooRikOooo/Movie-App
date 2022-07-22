package com.example.filmshelper.data.dataSources.mainScreen

import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.data.room.FilmsDao
import com.example.filmshelper.data.room.FilmsDataBase
import com.example.filmshelper.domain.dataSources.FilmsDataSource
import java.io.IOException
import javax.inject.Inject

class LocaleDataSourceImpl @Inject constructor(
    private val dataBase: FilmsDataBase
) : FilmsDataSource {
    override suspend fun getPopularFilms(): Result<MostPopularMovies> {
        val data = MostPopularMovies("okay", dataBase.filmsDao().readAllPopularFilms())

        return try {
            Result.success(data)
        } catch (e : IOException){
            Result.failure(e)
        }
    }

    fun addOrUpdateLocalePopularFilms(film : ItemPopularMovies){
        dataBase.filmsDao().addOrUpdatePopularFilms(film)
    }

    override suspend fun getNowShowingFilms(): Result<NowShowingMovies> {
        val data = NowShowingMovies("okay", dataBase.filmsDao().readAllNowShowingFilms())

        return try {
            Result.success(data)
        } catch (e : IOException){
            Result.failure(e)
        }
    }

    fun addOrUpdateLocaleNowShowingFilms(film : ItemNowShowingMovies){
        dataBase.filmsDao().addOrUpdateNowShowingFilms(film)
    }


}