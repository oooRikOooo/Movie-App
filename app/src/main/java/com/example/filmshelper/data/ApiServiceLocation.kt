package com.example.filmshelper.data

import com.example.filmshelper.BuildConfig.GOOGLE_MAPS_API_KEY
import com.example.filmshelper.data.models.allMoviesTheatres.AllMoviesTheatre
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceLocation {
    @GET("maps/api/place/textsearch/json")
    fun getAllMovieTheatres(
        @Query("query", encoded = true) query: String,
        @Query("key") key: String = GOOGLE_MAPS_API_KEY
    ): Call<AllMoviesTheatre>
}