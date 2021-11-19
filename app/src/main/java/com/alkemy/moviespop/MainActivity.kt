package com.alkemy.moviespop

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alkemy.moviespop.adapter.MainRecyclerAdapter
import com.alkemy.moviespop.api.NetStatus
import com.alkemy.moviespop.databinding.ActivityMainBinding
import com.alkemy.moviespop.fragment.DetailFragment
import com.alkemy.moviespop.model.Movie
import com.alkemy.moviespop.viewModel.MainViewModel
import com.alkemy.moviespop.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity(), MainRecyclerAdapter.Callback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val movies = mutableListOf<Movie>()

    private val viewModel: MainViewModel by viewModels(
        factoryProducer = { MainViewModelFactory() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        progressBar = binding.pbMain
        progressBar.visibility = View.VISIBLE

        adapter = MainRecyclerAdapter(movies)
        adapter.callback = this

        recyclerView = binding.rvMovies
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        setObservers()
    }

    override fun onSupportNavigateUp(): Boolean {
        this.supportActionBar!!.title = "MoviesPop"
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        onBackPressed()
        return true
    }

    private fun setObservers() {
        viewModel.movieList.observe(this, {
            when (it.status) {
                NetStatus.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                NetStatus.SUCCESS -> {
                    progressBar.visibility = View.INVISIBLE
                    for (movie in it.data!!) {
                        movies.add(movie)
                    }
                    adapter.notifyDataSetChanged()
                }
                NetStatus.ERROR -> {
                    Toast.makeText(this, "Failed loading data", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun onMovieClicked(movie: Movie) {
        val transaction = supportFragmentManager
        val detailFragment = DetailFragment()
        val data = Bundle()

        data.putInt("Id", movie.id)
        data.putString("Image", movie.imageUrl)
        data.putString("Title", movie.title)
        data.putFloat("Vote average", movie.voteAverage)
        data.putString("Release Date", movie.releaseDate)
        data.putString("Language", movie.originalLanguage)
        data.putString("Overview", movie.overview)
        detailFragment.arguments = data

        transaction.beginTransaction()
            .replace(R.id.frag_container, detailFragment)
            .addToBackStack("detailFragment").commit()
    }

}