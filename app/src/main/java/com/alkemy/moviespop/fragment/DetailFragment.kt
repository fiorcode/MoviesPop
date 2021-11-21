package com.alkemy.moviespop.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alkemy.moviespop.Global
import com.alkemy.moviespop.MainActivity
import com.alkemy.moviespop.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import androidx.annotation.RequiresApi
import com.alkemy.moviespop.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import android.text.method.ScrollingMovementMethod





class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).supportActionBar!!.title = "Details"
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        val detailImage: ImageView = binding.ivMovieImage
        val detailBackImage: ImageView = binding.ivMovieImageBack
        val detailClasification: ImageView = binding.ivClasification
        val detailTextTitle: TextView = binding.tvMovieTitleDetail
        val detailTextVoteAverage: TextView = binding.tvVoteAverage
        val detailRatingBar: RatingBar = binding.detailRatingBar
        val detailTextDate: TextView = binding.tvDate
        val detailTextLang: TextView = binding.tvLang
        val detailTextOverview: TextView = binding.tvOverview
        val detailGenreList: TextView = binding.tvGenres

        if(arguments != null) {
            val imageString = arguments?.getString("Image")
            val backImageString = arguments?.getString("Back image")
            val adultBoolean = arguments?.getBoolean("Adult")
            val titleString = arguments?.getString("Title")
            val voteAverageFloat = arguments?.getFloat("Vote average")
            val dateString = arguments?.getString("Release Date")
            val langString = arguments?.getString("Language")
            val overviewString = arguments?.getString("Overview")
            val genresList = arguments?.getStringArrayList("Genres")

            detailTextOverview.movementMethod = ScrollingMovementMethod()

            Picasso.get().load("${Global.BASE_IMAGE_URL}$imageString").into(detailImage)
            Picasso.get().load("${Global.BASE_IMAGE_URL}$backImageString").into(detailBackImage)

            if(adultBoolean!!) detailClasification.setImageResource(R.drawable.nc_17)
            detailTextTitle.text = titleString
            detailTextVoteAverage.text = voteAverageFloat.toString()

            detailRatingBar.numStars = 5
            detailRatingBar.max = 5
            detailRatingBar.rating = voteAverageFloat!!*0.5f
            detailRatingBar.stepSize = 0.1f

            val date = LocalDate.parse(dateString)
            detailTextDate.text = "${date.month.toString().lowercase(Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }} ${date.dayOfWeek.value}, ${date.year}"

            val lang: String = langString!!
            val originalLang = Locale(lang, lang)
            detailTextLang.text = originalLang.displayName
            detailTextOverview.text = overviewString

            detailGenreList.text = genresList!!.joinToString(separator = ", ")

        }
        else {
            Toast.makeText(context, "Couldn't load info", Toast.LENGTH_SHORT).show()
        }

        return  binding.root
    }
}