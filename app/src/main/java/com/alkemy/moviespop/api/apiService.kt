package com.alkemy.moviespop.api

import com.alkemy.moviespop.Global
import com.alkemy.moviespop.model.Movie
import retrofit2.Call
import retrofit2.http.GET

interface apiService {
    @GET("/3/movie/popular?api_key=${Global.APY_KEY}")
    fun getPopularMovies(): Call<MovieListResponse>
}

data class MovieListResponse(
    val results: List<Movie>
)