package com.example.filmshelper.data.repository

import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.models.FilmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import com.example.filmshelper.domain.repository.MainScreenRepository
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class MainScreenRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
): MainScreenRepository {
    override suspend fun getMoviesInTheaters(): Result<NowShowingMovies> {
        return try {
            Result.success(apiService.getMoviesInTheaters())
        } catch (e : IOException){
            Result.failure(e)
        }

    }

    override suspend fun getPopularMovies(): Result<MostPopularMovies> {
        return try {
            Result.success(apiService.getPopularMovies())
        } catch (e : IOException){
            Result.failure(e)
        }
    }

    override suspend fun getMovieById(id:String): Result<FilmDetails> {
        return try {
            Result.success(apiService.getMovieById(id))
        } catch (e : IOException){
            Result.failure(e)
        }
    }

    override suspend fun getMovieTrailerById(id:String): Result<YoutubeTrailer> {
        return try {
            Result.success(apiService.getMovieTrailerById(id))
        } catch (e : IOException){
            Result.failure(e)
        }
    }
}