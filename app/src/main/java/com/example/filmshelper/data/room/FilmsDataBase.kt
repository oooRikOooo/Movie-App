package com.example.filmshelper.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.utils.NowShowingFilmsTypeConverters

@Database(entities = [ItemPopularMovies::class, ItemNowShowingMovies::class], version = 2, exportSchema = true)
@TypeConverters(NowShowingFilmsTypeConverters::class)
abstract class FilmsDataBase : RoomDatabase() {

    abstract fun filmsDao() : FilmsDao

    /*companion object{
        private var INSTANCE: FilmsDataBase? = null

        fun getInstance(context: Context): FilmsDataBase {

            val tmpInstance = INSTANCE
            if (tmpInstance != null)
                return tmpInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    FilmsDataBase::class.java,
                    "films_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }*/
}