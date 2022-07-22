package com.example.filmshelper.data.models.popularMovies

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.filmshelper.utils.PopularFilmsTypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "popular_films_table")
@TypeConverters(PopularFilmsTypeConverters::class)
data class ItemPopularMovies(
    @PrimaryKey(autoGenerate = true)
    val idFilm: Int,
    val contentRating: String,
    val description: String,
    val genreList: List<Genre>,
    val genres: String,
    @SerializedName("id")
    val movieId: String,
    val imDbRating: String,
    val imDbRatingVotes: String,
    val image: String,
    val metacriticRating: String,
    val plot: String,
    val runtimeStr: String,
    val starList: List<Star>,
    val stars: String,
    val title: String
)