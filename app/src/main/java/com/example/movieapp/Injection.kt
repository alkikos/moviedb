package com.example.movieapp

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.api.MovieDBService
import com.example.movieapp.data.MovieDBRepository
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.ui.ViewModelFactory

object Injection {
    private fun provideMovieDBRepository(context: Context): MovieDBRepository {
        return MovieDBRepository(MovieDBService.create(), MovieDatabase.getInstance(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideMovieDBRepository(context))
    }
}