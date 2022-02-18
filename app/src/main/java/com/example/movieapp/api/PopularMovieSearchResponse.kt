package com.example.movieapp.api

import com.example.movieapp.model.PopularMovie
import com.example.movieapp.model.SimilarMovie
import com.google.gson.annotations.SerializedName

data class PopularMovieSearchResponse(
    val page: Int,
    @SerializedName("results")val popularMovies: List<PopularMovie>,
    val total: Int = 0
)

data class SimilarMovieSearchResponse(
    val page: Int,
    @SerializedName("results")val similarMovies: List<SimilarMovie>,
    val total: Int = 0
)