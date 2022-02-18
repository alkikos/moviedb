package com.example.movieapp.ui.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.model.PopularMovie

class MovieAdapter(private val movieClick: MovieClick) : PagingDataAdapter<PopularMovie, MovieViewHolder>(
    MOVIE_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
       return MovieViewHolder.create(parent, movieClick)
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<PopularMovie>() {
            override fun areItemsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
                return oldItem.movieId == newItem.movieId
            }

            override fun areContentsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean =
                oldItem == newItem
        }
    }
}

