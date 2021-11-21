package com.alkemy.moviespop.api

import com.alkemy.moviespop.Global
import com.alkemy.moviespop.model.Genre
import com.alkemy.moviespop.model.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/3/movie/popular?api_key=${Global.APY_KEY}")
    fun getPopularMovies(@Query("page") page: String): Call<MovieListResponse>

    @GET("/3/genre/movie/list?api_key=${Global.APY_KEY}")
    fun getGenres(): Call<GenreListResponse>
}

data class MovieListResponse(
    val results: List<Movie>
)

data class GenreListResponse(
    val genres: List<Genre>
)