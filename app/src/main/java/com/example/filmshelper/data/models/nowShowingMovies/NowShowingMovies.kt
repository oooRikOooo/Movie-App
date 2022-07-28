package com.example.filmshelper.data.models.nowShowingMovies

data class NowShowingMovies(
    val errorMessage: String?,
    val queryString: String,
    val results: List<ItemNowShowingMovies>
)