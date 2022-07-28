package com.example.filmshelper.data

import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.PopularTvShows
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://imdb-api.com/en/API/"
private const val API_KEY2 = "k_8xhud37t"
private const val API_KEY1 = "k_zvyokdrf"

interface ApiService {

    @GET("AdvancedSearch/$API_KEY2?groups=now-playing-us&count=4")
    suspend fun getMoviesInTheaters(): NowShowingMovies

    @GET("AdvancedSearch/$API_KEY2?title_type=feature&groups=top_100&count=5")
    suspend fun getPopularMovies(): PopularMovies

    @GET("AdvancedSearch/$API_KEY2?title_type=tv_series&count=5")
    suspend fun getPopularTvShows(): PopularTvShows

    @GET("Title/$API_KEY2/{id}")
    suspend fun getMovieById(
       @Path("id") id:String
    ): FilmDetails

    @GET("YouTubeTrailer/$API_KEY2/{id}")
    suspend fun getMovieTrailerById(
        @Path("id") id: String
    ): YoutubeTrailer

}