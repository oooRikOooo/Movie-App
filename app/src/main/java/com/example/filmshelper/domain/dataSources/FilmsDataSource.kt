package com.example.filmshelper.domain.dataSources

import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies

interface FilmsDataSource {
    suspend fun getPopularFilms(): Result<MostPopularMovies>

    suspend fun getNowShowingFilms() : Result<NowShowingMovies>
}