package com.ahmadabuhasan.movietmdb.presentation.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmadabuhasan.movietmdb.R
import com.ahmadabuhasan.movietmdb.databinding.ItemMovieBinding
import com.ahmadabuhasan.movietmdb.domain.model.Movie
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class MovieListAdapter : PagingDataAdapter<Movie, MovieListAdapter.MovieViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val displayDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)

        fun bind(movie: Movie) {
            binding.textTitle.text = movie.title
            binding.textReleaseDate.text = formatReleaseDate(movie.releaseDate)
            binding.textRating.text = String.format(Locale.US, "★ %.1f", movie.voteAverage)

            Glide.with(binding.imagePoster)
                .load(movie.posterUrl)
                .placeholder(R.drawable.placeholder_poster)
                .error(R.drawable.placeholder_poster)
                .centerCrop()
                .into(binding.imagePoster)
        }

        // TMDB dates aren't guaranteed present/well-formed, so a bad value falls back to "-" rather than crashing bind().
        private fun formatReleaseDate(rawDate: String): String {
            if (rawDate.isBlank()) return "-"
            return try {
                apiDateFormat.parse(rawDate)?.let { displayDateFormat.format(it) } ?: "-"
            } catch (e: Exception) {
                "-"
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}
