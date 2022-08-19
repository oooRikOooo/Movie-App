package com.example.filmshelper.data

import com.example.filmshelper.data.models.allFilmsWithQuery.FilmsWithQuery
import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.PopularTvShows
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://imdb-api.com/en/API/"
private const val API_KEY2 = "k_zny5h151"
private const val API_KEY1 = "k_57b5u7uy"

interface ApiService {

    @GET("AdvancedSearch/$API_KEY2?groups=now-playing-us")
    suspend fun getMoviesInTheaters(
        @Query("count") count: Int = 4
    ): NowShowingMovies

    @GET("AdvancedSearch/$API_KEY2?title_type=feature&groups=top_100")
    suspend fun getPopularMovies(
        @Query("count") count: Int = 5
    ): PopularMovies


    @GET("AdvancedSearch/$API_KEY2?title_type=tv_series&count=5")
    suspend fun getPopularTvShows(): PopularTvShows

    @GET("Title/$API_KEY2/{id}")
    suspend fun getMovieById(
        @Path("id") id: String
    ): FilmDetails

    @GET("YouTubeTrailer/$API_KEY2/{id}")
    suspend fun getMovieTrailerById(
        @Path("id") id: String
    ): YoutubeTrailer

    @GET("AdvancedSearch/$API_KEY2")
    suspend fun getFilmsWithQuery(
        @Query("title") title: String,
        @Query("title_type", encoded = true) titleType: String,
        @Query("genres", encoded = true) genres: String,
        @Query("count") count: Int = 16
    ): FilmsWithQuery


}