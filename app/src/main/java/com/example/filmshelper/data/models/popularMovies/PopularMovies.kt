package com.example.filmshelper.data.models.popularMovies

data class PopularMovies(
    val errorMessage: Any,
    val queryString: String,
    val results: List<ItemPopularMovies>
)