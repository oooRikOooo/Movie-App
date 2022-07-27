package com.example.filmshelper.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows
import com.example.filmshelper.utils.NowShowingFilmsTypeConverters
import com.example.filmshelper.utils.PopularFilmsTypeConverters
import com.example.filmshelper.utils.PopularTvShowsTypeConverters

@Database(
    entities = [ItemPopularMovies::class, ItemNowShowingMovies::class, ItemPopularTvShows::class],
    version = 12,
    exportSchema = true
)
@TypeConverters(
    NowShowingFilmsTypeConverters::class,
    PopularFilmsTypeConverters::class,
    PopularTvShowsTypeConverters::class
)
abstract class FilmsDataBase : RoomDatabase() {

    abstract fun filmsDao() : FilmsDao

}