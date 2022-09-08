package com.example.filmshelper.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows


@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdatePopularFilms(film: ItemPopularMovies)

    @Query("DELETE FROM popular_films_table")
    fun deleteAllPopularFilms(): Int

    @Query("SELECT * FROM popular_films_table")
    fun readAllPopularFilms(): List<ItemPopularMovies>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateNowShowingFilms(film: ItemNowShowingMovies)

    @Query("DELETE FROM now_showing_movies_table")
    fun deleteAllNowShowingFilms(): Int

    @Query("SELECT * FROM now_showing_movies_table")
    fun readAllNowShowingFilms(): List<ItemNowShowingMovies>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdatePopularTvShows(tvShow: ItemPopularTvShows)

    @Query("DELETE FROM popular_tv_shows_table")
    fun deleteAllPopularTvShows(): Int

    @Query("SELECT * FROM popular_tv_shows_table")
    fun readAllPopularTvShows(): List<ItemPopularTvShows>
}