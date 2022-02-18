package com.example.movieapp.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.model.PopularMovie
import com.example.movieapp.model.SimilarMovie

@Dao
interface PopularMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(popularMovies: List<PopularMovie>)

    @Query("SELECT * FROM popular_movies order by popularity desc")
    fun movies(): PagingSource<Int, PopularMovie>

    @Query("DELETE FROM popular_movies")
    suspend fun clearMovies()

    @Query("SELECT * FROM popular_movies where movieId=:movieId")
    suspend fun getMovie(movieId: Long): PopularMovie
}


@Dao
interface SimilarMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(popularMovies: List<SimilarMovie>)

    @Query("SELECT * FROM similar_movies order by roomId")
    fun movies(): PagingSource<Int, SimilarMovie>

    @Query("DELETE FROM similar_movies")
    suspend fun clearMovies()

    @Query("SELECT * FROM similar_movies where movieId=:movieId")
    suspend fun getMovie(movieId: Long): SimilarMovie
}
