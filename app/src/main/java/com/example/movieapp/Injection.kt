package com.example.movieapp

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.api.MovieDBService
import com.example.movieapp.data.MovieDBRepository
import com.example.movieapp.data.similar.SimilarMovieDBRepository
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.ui.detail.DetailViewModelFactory
import com.example.movieapp.ui.list.ViewModelFactory

object Injection {
    private fun provideMovieDBRepository(context: Context): MovieDBRepository {
        return MovieDBRepository(MovieDBService.create(), MovieDatabase.getInstance(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideMovieDBRepository(context))
    }
}

object SimilarInjection {
    private fun provideMovieDBRepository(context: Context): SimilarMovieDBRepository {
        return SimilarMovieDBRepository(MovieDBService.create(), MovieDatabase.getInstance(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return DetailViewModelFactory(provideMovieDBRepository(context))
    }
}