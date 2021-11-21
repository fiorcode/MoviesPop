package com.alkemy.moviespop.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alkemy.moviespop.api.MovieData
import com.alkemy.moviespop.api.MovieRepository
import com.alkemy.moviespop.api.NetStatus
import com.alkemy.moviespop.api.Resource
import com.alkemy.moviespop.model.Genre

class MainViewModel constructor(private val repository: MovieRepository): ViewModel() {

    var genreList = MutableLiveData<Resource<List<Genre>>>()

    init {
        getGenreList()
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