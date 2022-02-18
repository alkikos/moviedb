package com.example.movieapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.room.withTransaction
import com.bumptech.glide.Glide
import com.example.movieapp.Injection
import com.example.movieapp.SimilarInjection
import com.example.movieapp.api.Api
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.model.Movie
import com.example.movieapp.model.SimilarMovie
import com.example.movieapp.ui.list.MovieAdapter
import com.example.movieapp.ui.list.MovieClick
import com.example.movieapp.ui.list.MovieDBViewModel
import com.example.movieapp.ui.list.loadImage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


class MovieDetailFragment : Fragment(), MovieClick {

    private lateinit var binding: FragmentMovieDetailBinding


    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var viewModel: DetailMovieDBViewModel


    private val adapter = SimilarMovieAdapter<SimilarMovie>(this)

    private var searchJob: Job? = null

    private fun search(movieId: Long) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchSimilarMovies(movieId).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private lateinit var movieDatabase: MovieDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailBinding.inflate(inflater)
        movieDatabase = MovieDatabase.getInstance(requireContext())



        viewModel = ViewModelProvider(this, SimilarInjection.provideViewModelFactory(requireContext()))
            .get(DetailMovieDBViewModel::class.java)

        binding.similarMovies.adapter = adapter

        search(args.movieId)

        viewModel.getPopularMovie(args.movieId)
        viewModel.move.observe(viewLifecycleOwner,{
            it?.let {
                binding.movie = it
                binding.executePendingBindings()
            }
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun movieClick(movieId: Long) {
        viewModel.getSimilarMovie(movieId)
        binding.scrollable.fullScroll(View.FOCUS_DOWN)
    }


}