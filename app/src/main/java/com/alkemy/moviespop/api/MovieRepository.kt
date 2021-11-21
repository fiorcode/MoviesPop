package com.alkemy.moviespop.api

import com.alkemy.moviespop.model.Genre

class MovieRepository constructor(private val data: MovieData) {
    fun getGenreList(netResponse: MovieData.NetResponse<List<Genre>>) {
        return data.getGenreList(netResponse)
    }
}