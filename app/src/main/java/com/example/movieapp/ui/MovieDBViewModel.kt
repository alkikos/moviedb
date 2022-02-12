package com.example.movieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.data.MovieDBRepository
import com.example.movieapp.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieDBViewModel(private val repository: MovieDBRepository) : ViewModel() {
    private var currentSearchResult: Flow<PagingData<Movie>>? = null

    fun searchMovies(): Flow<PagingData<Movie>> {
        val lastResult = currentSearchResult
        val newResult: Flow<PagingData<Movie>> = repository.getMovieResultStream()
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
