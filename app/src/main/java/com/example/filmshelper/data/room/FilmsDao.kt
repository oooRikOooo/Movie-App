package com.example.filmshelper.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import dagger.Provides


@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdatePopularFilms(film : ItemPopularMovies)

    @Query("SELECT * FROM popular_films_table")
    fun readAllPopularFilms() : List<ItemPopularMovies>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateNowShowingFilms(film : ItemNowShowingMovies)

    @Query("SELECT * FROM now_showing_movies_table")
    fun readAllNowShowingFilms() : List<ItemNowShowingMovies>
}