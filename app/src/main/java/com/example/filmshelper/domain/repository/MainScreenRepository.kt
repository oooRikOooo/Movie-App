package com.example.filmshelper.domain.repository

import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popular.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows
import com.example.filmshelper.data.models.popular.popularTvShows.PopularTvShows
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer

interface MainScreenRepository {

    suspend fun getMoviesInTheaters(): Result<NowShowingMovies>

    suspend fun getPopularMovies(): Result<PopularMovies>

    suspend fun getMovieById(id:String): Result<FilmDetails>

    suspend fun getMovieTrailerById(id:String): Result<YoutubeTrailer>

    suspend fun getPopularTvShow(): Result<PopularTvShows>

    fun addOrUpdateLocalePopularTvShows(tvShow: ItemPopularTvShows)

    fun addOrUpdateLocalePopularFilms(film : ItemPopularMovies)

    fun addOrUpdateLocaleNowShowingFilms(film: ItemNowShowingMovies)
}