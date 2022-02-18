package com.example.movieapp.ui.detail

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.model.Movie
import com.example.movieapp.ui.list.MovieClick

class SimilarMovieAdapter<T: Movie>(private val movieClick: MovieClick) : PagingDataAdapter<T, SimilarMovieViewHolder>(
    MOVIE_COMPARATOR()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        return SimilarMovieViewHolder.create(parent, movieClick)
    }


    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private fun <T: Movie> MOVIE_COMPARATOR() = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem.movieId == newItem.movieId
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                oldItem == newItem
        }
    }
}