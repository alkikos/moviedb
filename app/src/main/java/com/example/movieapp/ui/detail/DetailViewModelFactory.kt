package com.example.movieapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.MovieDBRepository
import com.example.movieapp.data.similar.SimilarMovieDBRepository
import com.example.movieapp.ui.list.MovieDBViewModel

class DetailViewModelFactory(private val repository: SimilarMovieDBRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailMovieDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailMovieDBViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}