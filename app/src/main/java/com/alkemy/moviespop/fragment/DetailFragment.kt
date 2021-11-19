package com.alkemy.moviespop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alkemy.moviespop.Global
import com.alkemy.moviespop.MainActivity
import com.alkemy.moviespop.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var image: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).supportActionBar!!.title = "Details"
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        val detailImage: ImageView = binding.ivMovieImage
        val detailTextTitle: TextView = binding.tvMovieTitleDetail
        val detailTextVoteAverage: TextView = binding.tvVoteAverage
        val detailTextDate: TextView = binding.tvDate
        val detailTextLang: TextView = binding.tvLang
        val detailTextOverview: TextView = binding.tvOverview

        if(arguments != null) {
            val imageString = arguments?.getString("Image")
            val titleString = arguments?.getString("Title")
            val voteAverageString = arguments?.getFloat("Vote average")
            val dateString = arguments?.getString("Release Date")
            val langString = arguments?.getString("Language")
            val overviewString = arguments?.getString("Overview")

            Picasso.get().load("${Global.BASE_IMAGE_URL}$imageString").into(detailImage)
            if(imageString != null) {
                image = imageString
            }
            detailTextTitle.text = titleString
            detailTextVoteAverage.text = voteAverageString.toString()
            detailTextDate.text = dateString
            detailTextLang.text = langString
            detailTextOverview.text = overviewString

        }
        else {
            Toast.makeText(context, "Couldn't load info", Toast.LENGTH_SHORT).show()
        }
        return  binding.root
    }
}