package com.example.filmshelper.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.utils.NowShowingFilmsTypeConverters
import com.example.filmshelper.utils.PopularFilmsTypeConverters

@Database(entities = [ItemPopularMovies::class, ItemNowShowingMovies::class], version = 4, exportSchema = true)
@TypeConverters(NowShowingFilmsTypeConverters::class, PopularFilmsTypeConverters::class)
abstract class FilmsDataBase : RoomDatabase() {

    abstract fun filmsDao() : FilmsDao

}