package com.example.movieapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val apiKey = "ae20305f42c7fc7180a305e413152439";
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

interface MovieDBService {

    @GET("3/tv/popular?api_key=$apiKey")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): PopularMovieSearchResponse

    @GET("/3/tv/{tv_id}/similar")
    suspend fun getSimilarMovies()

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/"

        fun create(): MovieDBService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieDBService::class.java)
        }
    }
}

suspend fun main() {

    print(MovieDBService.create().getPopularMovies(page = 1).movies.count())

}