package com.example.movieapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.api.MovieDBService
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.model.PopularMovie
import kotlinx.coroutines.flow.Flow

class MovieDBRepository(
    private val service: MovieDBService,
    private val database: MovieDatabase
){
    fun getMovieResultStream(): Flow<PagingData<PopularMovie>> {

        val pagingSourceFactory = { database.popularMovieDao().movies() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MovieDBRemoteMediator(
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}

