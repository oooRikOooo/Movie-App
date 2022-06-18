package com.example.filmshelper.data

import com.example.filmshelper.data.models.FilmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.data.models.popularTvShows.MostPopularTvShows
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://imdb-api.com/en/API/"
private const val API_KEY2 = "k_8xhud37t"
private const val API_KEY1 = "k_zvyokdrf"

interface ApiService {

    @GET("InTheaters/$API_KEY2")
    suspend fun getMoviesInTheaters(): NowShowingMovies

    @GET("MostPopularMovies/$API_KEY2")
    suspend fun getPopularMovies(): MostPopularMovies

    @GET("MostPopularTVs/$API_KEY2")
    suspend fun getPopularTvShows(): MostPopularTvShows


    @GET("Title/$API_KEY2/{id}")
    suspend fun getMovieById(
       @Path("id") id:String
    ): FilmDetails

    @GET("YouTubeTrailer/$API_KEY2/{id}")
    suspend fun getMovieTrailerById(
        @Path("id") id: String
    ): YoutubeTrailer

    /*companion object {
        operator fun invoke(): ApiService = Retrofit.Builder()
            .baseUrl("https://imdb-api.com/en/API/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }*/

}