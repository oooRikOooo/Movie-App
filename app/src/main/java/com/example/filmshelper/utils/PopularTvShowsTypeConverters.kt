package com.example.filmshelper.utils

import androidx.room.TypeConverter
import com.example.filmshelper.data.models.popular.popularTvShows.Genre
import com.example.filmshelper.data.models.popular.popularTvShows.Star
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PopularTvShowsTypeConverters {

    @TypeConverter
    fun fromListGenreToJSON(genreList: List<Genre>): String {
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun fromJSONToListGenre(json: String): List<Genre> {
        return Gson().fromJson(json, object : TypeToken<List<Genre>>() {}.type)
    }

    @TypeConverter
    fun fromListStarToJSON(starList: List<Star>?): String {
        return Gson().toJson(starList)
    }

    @TypeConverter
    fun fromJSONToListStar(json: String): List<Star> {
        return Gson().fromJson(json, object : TypeToken<List<Star>>() {}.type)
    }
}