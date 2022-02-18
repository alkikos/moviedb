package com.example.movieapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.model.PopularMovie
import com.example.movieapp.model.SimilarMovie

@Database(
    entities = [PopularMovie::class, PopularMovieRemoteKeys::class, SimilarMovie::class, SimilarMovieRemoteKeys::class],
    version =11,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun popularMovieDao(): PopularMovieDao
    abstract fun popularRemoteKeysDao(): PopularMovieRemoteKeysDao

    abstract fun similarMovieDao(): SimilarMovieDao
    abstract fun similarRemoteKeysDao(): SimilarMovieRemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java, "Movie.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}