package com.example.movieapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.MovieDBRepository

class ViewModelFactory(private val repository: MovieDBRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDBViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

