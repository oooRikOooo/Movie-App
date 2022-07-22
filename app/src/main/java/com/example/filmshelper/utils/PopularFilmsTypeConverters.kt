package com.example.filmshelper.utils

import androidx.room.TypeConverter
import com.example.filmshelper.data.models.popularMovies.Genre
import com.example.filmshelper.data.models.popularMovies.Star
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PopularFilmsTypeConverters {

    @TypeConverter
    fun fromListGenreToJson(genreList : List<Genre>) : String{
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun fromJsonToListGenre(json : String) : List<Genre>{
        return Gson().fromJson(json, object : TypeToken<List<Genre>>() {}.type)
    }

    @TypeConverter
    fun fromListStarToJson(starList : List<Star>) : String{
        return Gson().toJson(starList)
    }

    @TypeConverter
    fun fromJsonToListStar(json : String) : List<Star>{
        return Gson().fromJson(json, object : TypeToken<List<Star>>() {}.type)
    }
}