package com.example.filmshelper.data.models.popular.popularTvShows

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.utils.PopularTvShowsTypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "popular_tv_shows_table", indices = [Index(value = ["title"], unique = true)])
@TypeConverters(PopularTvShowsTypeConverters::class)
data class ItemPopularTvShows(
    @PrimaryKey(autoGenerate = true)
    val idFilm: Int,
    val description: String?,
    @SerializedName("id")
    val movieId: String?,
    val imDbRating: String?,
    val image: String?,
    val runtimeStr: String?,
    val title: String?
) : DisplayableItem