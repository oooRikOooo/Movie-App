package com.example.filmshelper.data.models.nowShowingMovies

data class NowShowingMovies(
    val errorMessage: Any,
    val queryString: String,
    val results: List<ItemNowShowingMovies>
)