package com.example.filmshelper.data.models.nowShowingMovies

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.filmshelper.utils.NowShowingFilmsTypeConverters
import com.google.gson.annotations.SerializedName


@Entity(tableName = "now_showing_movies_table")
@TypeConverters(NowShowingFilmsTypeConverters::class)
data class ItemNowShowingMovies(
    @PrimaryKey(autoGenerate = true)
    val idFilm: Int,
    val contentRating: String,
    val directorList: List<Director>,
    val directors: String,
    val fullTitle: String,
    val genreList: List<Genre>,
    val genres: String,
    @SerializedName("id")
    val movieId: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val metacriticRating: String,
    val plot: String,
    val releaseState: String,
    val runtimeMins: String,
    val runtimeStr: String,
    val starList: List<Star>,
    val stars: String,
    val title: String,
    val year: String
)