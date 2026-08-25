package com.ahmadabuhasan.movietmdb.presentation.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmadabuhasan.movietmdb.databinding.ItemReviewBinding
import com.ahmadabuhasan.movietmdb.domain.model.Review
import java.text.SimpleDateFormat
import java.util.Locale

class ReviewsAdapter : PagingDataAdapter<Review, ReviewsAdapter.ReviewViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ReviewViewHolder(
        private val binding: ItemReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        private val displayDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)

        fun bind(review: Review) {
            binding.textAuthor.text = review.author
            binding.textContent.text = review.content
            binding.textCreatedDate.text = formatCreatedDate(review.createdDate)

            binding.textRating.isVisible = review.rating != null
            review.rating?.let {
                binding.textRating.text = String.format(Locale.US, "★ %.1f/10", it)
            }
        }

        // TMDB created_at is ISO-8601; a bad/missing value falls back to "-" rather than crashing bind().
        private fun formatCreatedDate(rawDate: String): String {
            if (rawDate.isBlank()) return "-"
            return try {
                apiDateFormat.parse(rawDate)?.let { displayDateFormat.format(it) } ?: "-"
            } catch (e: Exception) {
                "-"
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
    }
}
