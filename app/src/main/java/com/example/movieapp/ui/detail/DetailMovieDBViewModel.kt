package com.example.movieapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.data.similar.SimilarMovieDBRepository
import com.example.movieapp.model.Movie
import com.example.movieapp.model.SimilarMovie
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class DetailMovieDBViewModel(private val repository: SimilarMovieDBRepository) : ViewModel() {

    fun searchSimilarMovies(movieId: Long): Flow<PagingData<SimilarMovie>> {
        return repository.getMovieResultStream(movieId)
            .cachedIn(viewModelScope)
    }

    private val _movie: MutableLiveData<Movie> = MutableLiveData()
    val move: LiveData<Movie>
        get() = _movie

    fun getPopularMovie(movieId: Long) = viewModelScope.launch {
        _movie.postValue(repository.getPopularMovie(movieId))
    }

    fun getSimilarMovie(movieId: Long) = viewModelScope.launch {
        _movie.postValue(repository.getSimilarMovie(movieId))
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCleared() {
        super.onCleared()
        GlobalScope.launch {
            repository.clearDatabase()
        }
    }
}
