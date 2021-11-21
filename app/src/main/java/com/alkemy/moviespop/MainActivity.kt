package com.alkemy.moviespop

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.alkemy.moviespop.adapter.MainRecyclerAdapter
import com.alkemy.moviespop.api.*
import com.alkemy.moviespop.databinding.ActivityMainBinding
import com.alkemy.moviespop.fragment.DetailFragment
import com.alkemy.moviespop.model.Genre
import com.alkemy.moviespop.model.Movie
import com.alkemy.moviespop.viewModel.MainViewModel
import com.alkemy.moviespop.viewModel.MainViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MainRecyclerAdapter.Callback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var movies = mutableListOf<Movie>()
    private val genres = mutableListOf<Genre>()

    private var visibleItemCount: Int = 0
    private var pastVisibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false
    private var pageId: Int = 1

    private val viewModel: MainViewModel by viewModels(
        factoryProducer = { MainViewModelFactory() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        layoutManager = GridLayoutManager(this, 2)

        progressBar = binding.pbMain
        progressBar.visibility = View.VISIBLE

        recyclerView = binding.rvMovies
        recyclerView.layoutManager = layoutManager

        swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            movies.clear()
            pageId = 1
            getMovies(pageId.toString())
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.genreList.observe(this, {
            when (it.status) {
                NetStatus.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                NetStatus.SUCCESS -> {
                    progressBar.visibility = View.INVISIBLE
                    for (genre in it.data!!) {
                        genres.add(genre)
                    }
                }
                NetStatus.ERROR -> {
                    Toast.makeText(this, "Failed loading data", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })

        getMovies(pageId.toString())

//        setObservers()
    }

    override fun onSupportNavigateUp(): Boolean {
        this.supportActionBar!!.title = "MoviesPop"
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        onBackPressed()
        return true
    }

    private fun getMovies(page: String) {
        progressBar.visibility = View.VISIBLE
        var apiService: ApiService = RetrofitService.instance.create(ApiService::class.java)
        var call: Call<MovieListResponse> = apiService.getPopularMovies(page)
        call.enqueue(object: Callback<MovieListResponse> {
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                progressBar.visibility = View.INVISIBLE
                if(response!!.code() == 200) {
                    loading = true
                    setUpAdapter(response.body()!!.results)
                }
                else {
                    Toast.makeText(applicationContext, "Status code: ${response!!.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Failed loading data", Toast.LENGTH_SHORT).show()
            }
        })

    }

//    private fun setObservers() {
//        viewModel.movieList.observe(this, {
//            when (it.status) {
//                NetStatus.LOADING -> {
//                    progressBar.visibility = View.VISIBLE
//                }
//                NetStatus.SUCCESS -> {
//                    progressBar.visibility = View.INVISIBLE
//                    for (movie in it.data!!) {
//                        movies.add(movie)
//                    }
//                    setUpAdapter(movies)
//                }
//                NetStatus.ERROR -> {
//                    Toast.makeText(this, "Failed loading data", Toast.LENGTH_SHORT).show()
//                    progressBar.visibility = View.INVISIBLE
//                }
//            }
//        })
//    }

    private fun setUpAdapter(body: List<Movie>) {
        if(movies.size == 0) {
            movies = body as MutableList<Movie>
            adapter = MainRecyclerAdapter(movies)
            adapter.callback = this
            recyclerView.adapter = adapter
        }
        else {
            var currentPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            movies.addAll(body!!)
            adapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(currentPosition)
        }
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItemCount = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if(loading) {
                        if((visibleItemCount + pastVisibleItemCount) >= totalItemCount) {
                            loading = false
                            pageId++
                            getMovies(pageId.toString())
                        }
                    }
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
        data.putString("Back image", movie.imageBackUrl)
        data.putString("Title", movie.title)
        data.putFloat("Vote average", movie.voteAverage)
        data.putString("Release Date", movie.releaseDate)
        data.putString("Language", movie.originalLanguage)
        data.putString("Overview", movie.overview)
        data.putBoolean("Adult", movie.adult)

        movie.genresList = arrayListOf()
        for(g in genres) {
            if(movie.genres.contains(g.id)) movie.genresList.add(g.name)
        }
        data.putStringArrayList("Genres", movie.genresList)

        detailFragment.arguments = data

        transaction.beginTransaction()
            .replace(R.id.frag_container, detailFragment)
            .addToBackStack("detailFragment").commit()
    }

}