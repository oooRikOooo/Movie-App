package com.example.filmshelper.data.repository

import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.dataSources.mainScreen.LocaleDataSourceImpl
import com.example.filmshelper.data.dataSources.mainScreen.RemoteDataSourceImpl
import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.nowShowingMovies.NowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popular.popularMovies.PopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows
import com.example.filmshelper.data.models.popular.popularTvShows.PopularTvShows
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

        val localeResult = popularMovieLocaleDataSource.getNowShowingFilms()

        return if (localeResult.isSuccess) {
            localeResult
        } else popularMovieRemoteDataSource.getNowShowingFilms()

    }

    override suspend fun getPopularMovies(): Result<PopularMovies> {

        val localeResult = popularMovieLocaleDataSource.getPopularFilms()

        return if (localeResult.isSuccess) {
            localeResult
        } else popularMovieRemoteDataSource.getPopularFilms()

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

    override suspend fun getPopularTvShow(): Result<PopularTvShows> {

        val localeResult = popularMovieLocaleDataSource.getPopularTvShows()

        return if (localeResult.isSuccess) {
            localeResult
        } else popularMovieRemoteDataSource.getPopularTvShows()
    }

    override fun addOrUpdateLocalePopularTvShows(tvShow: ItemPopularTvShows) {
        popularMovieLocaleDataSource.addOrUpdatePopularTvShows(tvShow)
    }
}