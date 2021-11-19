package com.alkemy.moviespop.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alkemy.moviespop.api.MovieData
import com.alkemy.moviespop.api.MovieRepository
import com.alkemy.moviespop.api.NetStatus
import com.alkemy.moviespop.api.Resource
import com.alkemy.moviespop.model.Movie

class MainViewModel constructor(private val repository: MovieRepository): ViewModel() {

    var movieList = MutableLiveData<Resource<List<Movie>>>()

    init { getMovieList() }

    private fun getMovieList() {
        this.movieList.value = Resource(NetStatus.LOADING)
        val response = object: MovieData.NetResponse<List<Movie>> {
            override fun onResponse(value: Resource<List<Movie>>) {
                movieList.value = value
            }
        }
        repository.getMovieList(response)
    }
}