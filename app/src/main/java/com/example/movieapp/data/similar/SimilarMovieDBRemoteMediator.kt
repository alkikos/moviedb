package com.example.movieapp.data.similar

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movieapp.api.MovieDBService
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.db.SimilarMovieRemoteKeys
import com.example.movieapp.model.SimilarMovie
import retrofit2.HttpException
import java.io.IOException

private const val MOVIEDB_STARTING_PAGE_INDEX = 1


@OptIn(ExperimentalPagingApi::class)
class SimilarMovieDBRemoteMediator(
    private val service: MovieDBService,
    private val movieDatabase: MovieDatabase,
    private val movieId: Long
) : RemoteMediator<Int, SimilarMovie>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, SimilarMovie>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: MOVIEDB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }


        try {
            val apiResponse = service.getSimilarMovies(movieId = movieId,page = page)

            val movies = apiResponse.similarMovies

            when (loadType) {
                LoadType.PREPEND -> {
                    movies.map {
                        val roomId = getFirstMovieRoomId(state)?.roomId ?:0
                        movies.forEachIndexed{
                                index, movie ->
                            movie.roomId = roomId - (movies.size - index.toLong())
                        }
                    }
                }
                LoadType.APPEND -> {
                    val roomId = getLastMovieRoomId(state)?.roomId ?:0
                    movies.forEachIndexed{
                            index, movie ->
                        movie.roomId = roomId + index.toLong() + 1
                    }
                }
                LoadType.REFRESH -> {
                    movies.forEachIndexed{
                        index, movie ->
                        movie.roomId = index.toLong()
                    }
                }
            }
            val endOfPaginationReached = movies.isEmpty()
            movieDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.similarRemoteKeysDao().clearRemoteKeys()
                    movieDatabase.similarMovieDao().clearMovies()
                }
                val prevKey = if (page == MOVIEDB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    SimilarMovieRemoteKeys(
                        roomMovieId = it.roomId,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                movieDatabase.similarRemoteKeysDao().insertAll(keys)
                movieDatabase.similarMovieDao().insertAll(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private fun getFirstMovieRoomId(state: PagingState<Int, SimilarMovie>): SimilarMovie? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
    }

    private fun getLastMovieRoomId(state: PagingState<Int, SimilarMovie>): SimilarMovie? {
        return state.lastItemOrNull()
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, SimilarMovie>): SimilarMovieRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item

        val a = state.lastItemOrNull()?.let { movie ->
                // Get the remote keys of the last item retrieved
                movieDatabase.similarRemoteKeysDao().remoteKeysRepoId(movie.roomId)
            }
        return a
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, SimilarMovie>): SimilarMovieRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                movieDatabase.similarRemoteKeysDao().remoteKeysRepoId(movie.roomId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, SimilarMovie>
    ): SimilarMovieRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.roomId?.let { roomId ->
                movieDatabase.similarRemoteKeysDao().remoteKeysRepoId(roomId)
            }
        }
    }

}