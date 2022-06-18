package com.example.filmshelper.data.models.youtubeTrailer

data class YoutubeTrailer(
    val errorMessage: String,
    val fullTitle: String,
    val imDbId: String,
    val title: String,
    val type: String,
    val videoId: String,
    val videoUrl: String,
    val year: String
)