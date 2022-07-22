package com.example.filmshelper.data.models.popularMovies

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "popular_films_table")
data class ItemPopularMovies(
    @PrimaryKey(autoGenerate = true)
    val idFilm: Int,
    val crew: String,
    val fullTitle: String,
    @SerializedName("id")
    val movieId: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val rank: String,
    val rankUpDown: String,
    val title: String,
    val year: String
)