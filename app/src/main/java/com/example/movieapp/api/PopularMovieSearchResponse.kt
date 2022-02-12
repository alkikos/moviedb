package com.example.movieapp.api

import com.example.movieapp.model.Movie
import com.google.gson.annotations.SerializedName

data class PopularMovieSearchResponse(
    val page: Int,
    @SerializedName("results")val movies: List<Movie>,
    val total: Int = 0
)