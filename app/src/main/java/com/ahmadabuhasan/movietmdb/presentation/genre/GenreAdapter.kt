package com.ahmadabuhasan.movietmdb.presentation.genre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmadabuhasan.movietmdb.databinding.ItemGenreBinding
import com.ahmadabuhasan.movietmdb.domain.model.Genre

class GenreAdapter(
    private val onGenreClick: (Genre) -> Unit
) : ListAdapter<Genre, GenreAdapter.GenreViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GenreViewHolder(
        private val binding: ItemGenreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.textGenreName.text = genre.name
            binding.root.setOnClickListener { onGenreClick(genre) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Genre, newItem: Genre) = oldItem == newItem
    }
}
