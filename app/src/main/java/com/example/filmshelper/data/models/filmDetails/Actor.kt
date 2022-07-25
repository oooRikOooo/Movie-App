package com.example.filmshelper.data.models.filmDetails

import com.example.filmshelper.data.models.DisplayableItem

data class Actor(
    val asCharacter: String,
    val id: String,
    val image: String,
    val name: String
) : DisplayableItem