package com.alkemy.moviespop.api

import com.alkemy.moviespop.model.Movie

class MovieRepository constructor(private val data: MovieData) {
    fun getMovieList(netResponse: MovieData.NetResponse<List<Movie>>) {
        return data.getMovieList(netResponse)
    }
}