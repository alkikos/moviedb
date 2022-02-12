package com.example.movieapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.api.IMAGE_BASE_URL
import com.example.movieapp.api.MovieDBService
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.model.Movie

class MovieViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie?) {
        if (movie == null) {
            /*val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
            language.visibility = View.GONE
            stars.text = resources.getString(R.string.unknown)
            forks.text = resources.getString(R.string.unknown)*/
        } else {
            showMovieData(movie)
        }
    }

    private fun showMovieData(movie: Movie) {
        val imageView = binding.movieImage
        binding.run {
            movieTitle.text = movie.title
            movieVoteAverage.text = movie.averageVot.toString()
        }
        val url = IMAGE_BASE_URL+movie.imageEndPoint
        Glide.with(imageView.context).load(url).into(imageView)
    }
    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val view = MovieItemBinding.inflate(LayoutInflater.from(parent.context))
            return MovieViewHolder(view)
        }
    }
}


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) { // This methods should not have any return type, = declaration would make it return that object declaration.
    Glide.with(view.context).load(url).into(view)
}