package com.alkemy.moviespop.api

import com.alkemy.moviespop.model.Genre
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieData {

    interface NetResponse<T> {
        fun onResponse(value: Resource<T>)
    }

    fun getGenreList(netResponse: NetResponse<List<Genre>>) {
        val service = RetrofitService.instance
            .create(ApiService::class.java).getGenres()

        service.enqueue(object: Callback<GenreListResponse> {
            override fun onResponse(
                call: Call<GenreListResponse>,
                response: Response<GenreListResponse>
            ) {
                val resource = response.body()?.run {
                    if(genres.isNotEmpty()) {
                        Resource(NetStatus.SUCCESS, genres)
                    }
                    else {
                        Resource(NetStatus.ERROR)
                    }
                } ?: run {
                    Resource(NetStatus.ERROR)
                }
                netResponse.onResponse(resource)
            }

            override fun onFailure(call: Call<GenreListResponse>, t: Throwable) {
                netResponse.onResponse(Resource(NetStatus.ERROR, message = t.message))
            }
        })
    }
}

data class Resource<out T> (
    val status: NetStatus,
    val data: T? = null,
    val message: String? = null)

enum class NetStatus { LOADING, SUCCESS, ERROR}