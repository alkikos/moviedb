
package com.example.movieapp.api

object Api {
  const val BASE_URL = "https://api.themoviedb.org/"
  private const val BASE_BACKDROP_PATH = "https://image.tmdb.org/t/p/w500"


  @JvmStatic
  fun getBackdropPath(backdropPath: String?): String {
    return BASE_BACKDROP_PATH + backdropPath
  }
}
