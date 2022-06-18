package com.example.filmshelper.domain.repository

import com.example.filmshelper.data.models.FilmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import retrofit2.Response

interface MainScreenRepository {

    suspend fun getMoviesInTheaters(): NowShowingMovies

    suspend fun getPopularMovies(): MostPopularMovies

    suspend fun getMovieById(id:String): FilmDetails

    suspend fun getMovieTrailerById(id:String): YoutubeTrailer
}