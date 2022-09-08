package com.example.filmshelper.data.models.popular.popularTvShows

data class PopularTvShows(
    val errorMessage: String?,
    val queryString: String,
    val results: List<ItemPopularTvShows>
)