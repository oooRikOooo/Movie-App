package com.example.filmshelper.data.dataSources.mainScreen

import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.PopularTvShows
import com.example.filmshelper.domain.dataSources.FilmsDataSource
import java.io.IOException
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : FilmsDataSource{

    override suspend fun getPopularFilms(): Result<PopularMovies> {
        return try {
            val data = apiService.getPopularMovies(count = 5)
            Result.success(data)
        } catch (e : IOException){
            Result.failure(e)
        }

    }

    override suspend fun getNowShowingFilms(): Result<NowShowingMovies> {
        return try {
            Result.success(apiService.getMoviesInTheaters())
        } catch (e: IOException){
            Result.failure(e)
        }
    }

    override suspend fun getPopularTvShows(): Result<PopularTvShows> {
        return try {
            Result.success(apiService.getPopularTvShows())
        } catch (e : IOException){
            Result.failure(e)
        }
    }
}