package com.example.filmshelper.data.models.filmDetails

import com.example.filmshelper.data.models.DisplayableItem

data class Similar(
    val id: String,
    val imDbRating: String,
    val image: String,
    val title: String
) : DisplayableItem