package com.example.movieapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PopularMovieRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(popularMovieRemoteKey: List<PopularMovieRemoteKeys>)

    @Query("SELECT * FROM remote_keys_popular_movie WHERE movieId = :movieId")
    suspend fun remoteKeysRepoId(movieId: Long): PopularMovieRemoteKeys?

    @Query("DELETE FROM remote_keys_popular_movie")
    suspend fun clearRemoteKeys()
}

@Dao
interface SimilarMovieRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(popularMovieRemoteKeyRemoteKeys: List<SimilarMovieRemoteKeys>)

    @Query("SELECT * FROM remote_keys_similar_movie WHERE roomMovieId = :roomMovieId")
    suspend fun remoteKeysRepoId(roomMovieId: Long): SimilarMovieRemoteKeys?

    @Query("DELETE FROM remote_keys_similar_movie")
    suspend fun clearRemoteKeys()
}
