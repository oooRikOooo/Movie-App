package com.example.filmshelper.data.dataSources.mainScreen

import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.domain.dataSources.FilmsDataSource
import java.io.IOException
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : FilmsDataSource{

    override suspend fun getPopularFilms(): Result<MostPopularMovies> {
        return try {
            Result.success(apiService.getPopularMovies())
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
}