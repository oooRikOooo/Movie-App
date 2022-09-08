package com.example.filmshelper.data.models.popular.popularMovies


data class PopularMovies(
    val errorMessage: String?,
    val queryString: String,
    val results: List<ItemPopularMovies>
)