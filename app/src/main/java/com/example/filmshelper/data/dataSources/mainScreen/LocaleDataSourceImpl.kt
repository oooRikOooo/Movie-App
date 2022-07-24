package com.example.filmshelper.data.dataSources.mainScreen

import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popular.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows
import com.example.filmshelper.data.models.popular.popularTvShows.PopularTvShows
import com.example.filmshelper.data.room.FilmsDataBase
import com.example.filmshelper.domain.dataSources.FilmsDataSource
import java.io.IOException
import javax.inject.Inject

class LocaleDataSourceImpl @Inject constructor(
    private val dataBase: FilmsDataBase
) : FilmsDataSource {
    override suspend fun getPopularFilms(): Result<PopularMovies> {
        val data = PopularMovies("okay","query", dataBase.filmsDao().readAllPopularFilms())

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
        val data = NowShowingMovies("okay","?groups=now-playing-us&count=5", dataBase.filmsDao().readAllNowShowingFilms())

        return try {
            Result.success(data)
        } catch (e : IOException){
            Result.failure(e)
        }
    }

    override suspend fun getPopularTvShows(): Result<PopularTvShows> {
        val data = PopularTvShows("okay", "?title_type=tv_series&count=5", dataBase.filmsDao().readAllPopularTvShows())

        return try {
            Result.success(data)
        } catch (e: IOException){
            Result.failure(e)
        }
    }

    fun addOrUpdatePopularTvShows(tvShow: ItemPopularTvShows){
        dataBase.filmsDao().addOrUpdatePopularTvShows(tvShow)
    }

    fun addOrUpdateLocaleNowShowingFilms(film : ItemNowShowingMovies){
        dataBase.filmsDao().addOrUpdateNowShowingFilms(film)
    }


}