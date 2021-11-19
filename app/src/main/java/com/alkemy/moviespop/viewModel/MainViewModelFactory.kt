package com.alkemy.moviespop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alkemy.moviespop.api.MovieData
import com.alkemy.moviespop.api.MovieRepository

class MainViewModelFactory: ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val data = MovieData()
        val repository = MovieRepository(data)
        return MainViewModel(repository) as T
    }
}