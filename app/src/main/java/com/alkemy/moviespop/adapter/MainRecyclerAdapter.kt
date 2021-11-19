package com.alkemy.moviespop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alkemy.moviespop.Global
import com.alkemy.moviespop.R
import com.alkemy.moviespop.databinding.MovieItemBinding
import com.alkemy.moviespop.model.Movie
import com.squareup.picasso.Picasso

class MainRecyclerAdapter(private var Movies: List<Movie>) : RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    interface Callback {
        fun onMovieClicked(movieClicked: Movie)
    }

    var callback: Callback? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = MovieItemBinding.bind(view)

        val movieTitle: TextView = binding.tvTitle
        val movieImage: ImageView = binding.ivImage

        fun bind(movie: Movie) {
            Picasso.get().load("${Global.BASE_IMAGE_URL}${movie.imageUrl}").into(movieImage)
            movieTitle.text = movie.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(Movies[position])
        holder.movieImage.setOnClickListener {
            callback?.onMovieClicked(Movies[position])
        }
        holder.movieTitle.setOnClickListener {
            callback?.onMovieClicked(Movies[position])
        }
    }

    override fun getItemCount(): Int
    {
        return Movies.size
    }
}