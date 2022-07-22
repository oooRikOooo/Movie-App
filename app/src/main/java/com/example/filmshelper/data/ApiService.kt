package com.example.filmshelper.data

import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popularTvShows.MostPopularTvShows
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://imdb-api.com/en/API/"
private const val API_KEY2 = "k_8xhud37t"
private const val API_KEY1 = "k_zvyokdrf"

interface ApiService {

    /*@GET("InTheaters/$API_KEY1")
    suspend fun getMoviesInTheaters(): NowShowingMovies*/

    @GET("AdvancedSearch/$API_KEY2?groups=now-playing-us&count=5")
    suspend fun getMoviesInTheaters(): NowShowingMovies

    /*@GET("MostPopularMovies/$API_KEY1")
    suspend fun getPopularMovies(): MostPopularMovies*/

    @GET("AdvancedSearch/$API_KEY2?title_type=feature&groups=top_100&count=5")
    suspend fun getPopularMovies(): PopularMovies

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