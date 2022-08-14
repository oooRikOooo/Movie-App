package com.example.filmshelper.data.models.allFilmsWithQuery

import com.example.filmshelper.data.models.DisplayableItem

data class ItemFilmsWithQuery(
    val contentRating: String,
    val description: String,
    val genreList: List<Genre>,
    val genres: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingVotes: String,
    val image: String,
    val metacriticRating: String,
    val plot: String,
    val runtimeStr: String,
    val starList: List<Star>,
    val stars: String,
    val title: String
) : DisplayableItem