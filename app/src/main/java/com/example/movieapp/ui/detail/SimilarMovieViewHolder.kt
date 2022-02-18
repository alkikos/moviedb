package com.example.movieapp.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.api.Api
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.databinding.SimilarMovieItemBinding
import com.example.movieapp.model.Movie
import com.example.movieapp.ui.list.MovieClick

class SimilarMovieViewHolder(
    private val binding: SimilarMovieItemBinding,
    private val movieClick: MovieClick
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(Movie: Movie?) {
        if (Movie == null) {
            /*val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
            language.visibility = View.GONE
            stars.text = resources.getString(R.string.unknown)
            forks.text = resources.getString(R.string.unknown)*/
        } else {
            showMovieData(Movie)
        }
    }

    private fun showMovieData(movie: Movie) {
        binding.root.setOnClickListener {
            movieClick.movieClick(movie.movieId)

        }
        binding.movie = movie
    }

    companion object {
        fun create(parent: ViewGroup, movieClick: MovieClick): SimilarMovieViewHolder {
            val view = SimilarMovieItemBinding.inflate(LayoutInflater.from(parent.context))
            return SimilarMovieViewHolder(view, movieClick)
        }
    }
}