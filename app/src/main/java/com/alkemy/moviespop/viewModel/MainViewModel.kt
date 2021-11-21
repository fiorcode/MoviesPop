package com.alkemy.moviespop.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alkemy.moviespop.api.MovieData
import com.alkemy.moviespop.api.MovieRepository
import com.alkemy.moviespop.api.NetStatus
import com.alkemy.moviespop.api.Resource
import com.alkemy.moviespop.model.Genre
import com.alkemy.moviespop.model.Movie

class MainViewModel constructor(private val repository: MovieRepository): ViewModel() {

    var movieList = MutableLiveData<Resource<List<Movie>>>()
    var genreList = MutableLiveData<Resource<List<Genre>>>()

    init {
        getMovieList("1")
        getGenreList()
    }

    private fun getMovieList(page: String) {
        this.movieList.value = Resource(NetStatus.LOADING)
        val response = object: MovieData.NetResponse<List<Movie>> {
            override fun onResponse(value: Resource<List<Movie>>) {
                movieList.value = value
            }
        }
        repository.getMovieList(response, page)
    }

    private fun getGenreList() {
        this.genreList.value = Resource(NetStatus.LOADING)
        val response = object: MovieData.NetResponse<List<Genre>> {
            override fun onResponse(value: Resource<List<Genre>>) {
                genreList.value = value
            }
        }
        repository.getGenreList(response)
    }
}