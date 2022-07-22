package com.example.filmshelper.data.repository

import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl
import com.example.filmshelper.data.dataSources.mainScreen.RemoteDataSourceImpl
import com.example.filmshelper.data.models.FilmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popularMovies.MostPopularMovies
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import com.example.filmshelper.domain.repository.MainScreenRepository
import okio.IOException
import javax.inject.Inject

class MainScreenRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val popularMovieRemoteDataSource: RemoteDataSourceImpl,
    private val popularMovieLocaleDataSource: LocaleDataSourceImpl
): MainScreenRepository {

    override suspend fun getMoviesInTheaters(): Result<NowShowingMovies> {

        val remoteResult = popularMovieRemoteDataSource.getNowShowingFilms()
        val localeResult = popularMovieLocaleDataSource.getNowShowingFilms()

        return if (remoteResult.isSuccess){
            remoteResult
        } else localeResult

    }

    override suspend fun getPopularMovies(): Result<MostPopularMovies> {
        val remoteResult = popularMovieRemoteDataSource.getPopularFilms()
        val localeResult = popularMovieLocaleDataSource.getPopularFilms()

        return if (remoteResult.isSuccess){
            remoteResult
        } else localeResult

    }

    override fun addOrUpdateLocalePopularFilms(film : ItemPopularMovies){
        popularMovieLocaleDataSource.addOrUpdateLocalePopularFilms(film)
    }

    override fun addOrUpdateLocaleNowShowingFilms(film: ItemNowShowingMovies) {
        popularMovieLocaleDataSource.addOrUpdateLocaleNowShowingFilms(film)
    }

    override suspend fun getMovieById(id:String): Result<FilmDetails> {
        return try {
            Result.success(apiService.getMovieById(id))
        } catch (e : IOException){
            Result.failure(e)
        }
    }

    override suspend fun getMovieTrailerById(id:String): Result<YoutubeTrailer> {
        return try {
            Result.success(apiService.getMovieTrailerById(id))
        } catch (e : IOException){
            Result.failure(e)
        }
    }
}