package com.example.movieapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.api.Api
import com.example.movieapp.databinding.ItemPosterBinding
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.model.PopularMovie

class MovieViewHolder(private val binding: ItemPosterBinding, private val movieClick: MovieClick) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(popularMovie: PopularMovie?) {
        if (popularMovie == null) {
            /*val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
            language.visibility = View.GONE
            stars.text = resources.getString(R.string.unknown)
            forks.text = resources.getString(R.string.unknown)*/
        } else {
            showMovieData(popularMovie)
        }
    }

    private fun showMovieData(popularMovie: PopularMovie) {

        binding.movie = popularMovie
        binding.executePendingBindings()

        binding.root.setOnClickListener {
            movieClick.movieClick(popularMovie.movieId)
        }

    }

    companion object {
        fun create(parent: ViewGroup, movieClick: MovieClick): MovieViewHolder {
            val view = ItemPosterBinding.inflate(LayoutInflater.from(parent.context))
            return MovieViewHolder(view, movieClick)
        }
    }
}


@BindingAdapter("imageUrl")
fun loadImage(
    view: ImageView,
    url: String
) { // This methods should not have any return type, = declaration would make it return that object declaration.
    Glide.with(view.context).load(url).into(view)
}

interface MovieClick {
    fun movieClick(movieId: Long)
}

