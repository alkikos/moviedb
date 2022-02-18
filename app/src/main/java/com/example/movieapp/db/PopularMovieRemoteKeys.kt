package com.example.movieapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_popular_movie")
data class PopularMovieRemoteKeys(
    @PrimaryKey val movieId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)

@Entity(tableName = "remote_keys_similar_movie")
data class SimilarMovieRemoteKeys(
    @PrimaryKey val roomMovieId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)