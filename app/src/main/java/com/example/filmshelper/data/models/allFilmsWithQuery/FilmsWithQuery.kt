package com.example.filmshelper.data.models.allFilmsWithQuery

data class FilmsWithQuery(
    val errorMessage: Any,
    val queryString: String,
    val results: List<ItemFilmsWithQuery>
)