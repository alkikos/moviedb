package com.example.movieapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.movieapp.api.MovieDBService
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.model.PopularMovie
import retrofit2.HttpException
import java.io.IOException
import androidx.room.withTransaction
import com.example.movieapp.db.PopularMovieRemoteKeys


private const val MOVIEDB_STARTING_PAGE_INDEX = 1


@OptIn(ExperimentalPagingApi::class)
class MovieDBRemoteMediator(
    private val service: MovieDBService,
    private val movieDatabase: MovieDatabase
) : RemoteMediator<Int, PopularMovie>() {
    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PopularMovie>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: MOVIEDB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }


        try {
            val apiResponse = service.getPopularMovies(page)

            val movies = apiResponse.popularMovies
            val endOfPaginationReached = movies.isEmpty()
            movieDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.popularRemoteKeysDao().clearRemoteKeys()
                    movieDatabase.popularMovieDao().clearMovies()
                }
                val prevKey = if (page == MOVIEDB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    PopularMovieRemoteKeys(movieId = it.movieId, prevKey = prevKey, nextKey = nextKey)
                }
                movieDatabase.popularRemoteKeysDao().insertAll(keys)
                movieDatabase.popularMovieDao().insertAll(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PopularMovie>): PopularMovieRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item

        val a = state.lastItemOrNull()?.let { movie ->
            // Get the remote keys of the last item retrieved
            movieDatabase.popularRemoteKeysDao().remoteKeysRepoId(movie.movieId)
        }
        return a
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PopularMovie>): PopularMovieRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                movieDatabase.popularRemoteKeysDao().remoteKeysRepoId(movie.movieId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PopularMovie>
    ): PopularMovieRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.movieId?.let { movieId ->
                movieDatabase.popularRemoteKeysDao().remoteKeysRepoId(movieId)
            }
        }
    }

}