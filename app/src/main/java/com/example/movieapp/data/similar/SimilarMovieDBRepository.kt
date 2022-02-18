package com.example.movieapp.data.similar

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.example.movieapp.api.MovieDBService
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.model.SimilarMovie
import kotlinx.coroutines.flow.Flow

class SimilarMovieDBRepository(
    private val service: MovieDBService,
    private val database: MovieDatabase,
){
    fun getMovieResultStream(movieId: Long): Flow<PagingData<SimilarMovie>> {

        val pagingSourceFactory = { database.similarMovieDao().movies() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SimilarMovieDBRemoteMediator(
                service,
                database,
                movieId
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    suspend fun getPopularMovie(movieId: Long) = database.popularMovieDao().getMovie(movieId)
    suspend fun getSimilarMovie(movieId: Long) = database.similarMovieDao().getMovie(movieId)
    suspend fun clearDatabase() = database.withTransaction {
        database.similarMovieDao().clearMovies()
        database.similarRemoteKeysDao().clearRemoteKeys()
    }

}