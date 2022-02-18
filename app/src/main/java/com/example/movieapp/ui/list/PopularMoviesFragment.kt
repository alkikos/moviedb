package com.example.movieapp.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.Injection
import com.example.movieapp.databinding.FragmentPopularMoviesBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PopularMoviesFragment : Fragment(), MovieClick {

    private lateinit var binding: FragmentPopularMoviesBinding
    private lateinit var viewModel: MovieDBViewModel
    private val adapter = MovieAdapter(this)

    private var searchJob: Job? = null

    private fun search() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchMovies().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPopularMoviesBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext()))
            .get(MovieDBViewModel::class.java)

        binding.movieList.adapter = adapter

        search()
        return binding.root
    }

    override fun movieClick(movieId: Long) {
        val action =
            PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }
}