package com.alkemy.moviespop.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val adult: Boolean,
    var genresList: ArrayList<String>,
    @SerializedName("genre_ids") val genres: List<Int>,
    @SerializedName("poster_path") val imageUrl: String,
    @SerializedName("backdrop_path") val imageBackUrl: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val voteAverage: Float
)