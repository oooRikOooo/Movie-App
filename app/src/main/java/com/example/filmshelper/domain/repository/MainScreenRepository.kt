package com.example.filmshelper.domain.repository

import com.example.filmshelper.data.models.FilmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import retrofit2.Response

interface MainScreenRepository {

    suspend fun getMoviesInTheaters(): Result<NowShowingMovies>

    suspend fun getPopularMovies(): Result<MostPopularMovies>

    suspend fun getMovieById(id:String): Result<FilmDetails>

    suspend fun getMovieTrailerById(id:String): Result<YoutubeTrailer>

    fun addOrUpdateLocalePopularFilms(film : ItemPopularMovies)

    fun addOrUpdateLocaleNowShowingFilms(film: ItemNowShowingMovies)
}