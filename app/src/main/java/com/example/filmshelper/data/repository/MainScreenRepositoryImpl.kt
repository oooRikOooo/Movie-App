package com.example.filmshelper.data.repository

import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.models.FilmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import com.example.filmshelper.domain.repository.MainScreenRepository
import retrofit2.Response
import javax.inject.Inject

class MainScreenRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
): MainScreenRepository {
    override suspend fun getMoviesInTheaters(): NowShowingMovies {
        return apiService.getMoviesInTheaters()
    }

    override suspend fun getPopularMovies(): MostPopularMovies {
        return apiService.getPopularMovies()
    }

    override suspend fun getMovieById(id:String): FilmDetails {
        return apiService.getMovieById(id)
    }

    override suspend fun getMovieTrailerById(id:String): YoutubeTrailer {
        return apiService.getMovieTrailerById(id)
    }
}