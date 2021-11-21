package com.alkemy.moviespop.api

import com.alkemy.moviespop.model.Genre
import com.alkemy.moviespop.model.Movie

class MovieRepository constructor(private val data: MovieData) {
    fun getMovieList(netResponse: MovieData.NetResponse<List<Movie>>, page: String) {
        return data.getMovieList(netResponse, page)
    }

    fun getGenreList(netResponse: MovieData.NetResponse<List<Genre>>) {
        return data.getGenreList(netResponse)
    }
}