package com.example.movieapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.data.MovieDBRepository
import com.example.movieapp.model.PopularMovie
import kotlinx.coroutines.flow.Flow

class MovieDBViewModel(private val repository: MovieDBRepository) : ViewModel() {

    fun searchMovies(): Flow<PagingData<PopularMovie>> {
        return repository.getMovieResultStream()
            .cachedIn(viewModelScope)
    }
}

