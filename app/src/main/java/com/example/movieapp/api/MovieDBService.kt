package com.example.movieapp.api

import com.example.movieapp.BuildConfig
import com.example.movieapp.model.SimilarMovie
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieDBService {

    @GET("3/tv/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): PopularMovieSearchResponse

    @GET("/3/tv/{tv_id}/similar")
    suspend fun getSimilarMovies(
        @Path("tv_id") movieId: Long,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): SimilarMovieSearchResponse

    companion object {

        fun create(): MovieDBService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC


            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val url = originalUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                        .build()

                    val requestBuilder = originalRequest.newBuilder().url(url)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                })
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieDBService::class.java)
        }
    }
}

suspend fun main() {

    val service = MovieDBService.create()
    val result = service.getSimilarMovies(page = 1, movieId = 32613)
    print(result.similarMovies.count())

}