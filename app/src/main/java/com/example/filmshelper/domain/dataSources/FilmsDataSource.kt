package com.example.filmshelper.domain.dataSources

import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.PopularTvShows

interface FilmsDataSource {
    suspend fun getPopularFilms(): Result<PopularMovies>

    suspend fun getNowShowingFilms() : Result<NowShowingMovies>

    suspend fun getPopularTvShows(): Result<PopularTvShows>
}