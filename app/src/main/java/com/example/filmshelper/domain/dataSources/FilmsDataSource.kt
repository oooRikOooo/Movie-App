package com.example.filmshelper.domain.dataSources

import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.PopularMovies

interface FilmsDataSource {
    suspend fun getPopularFilms(): Result<PopularMovies>

    suspend fun getNowShowingFilms() : Result<NowShowingMovies>
}