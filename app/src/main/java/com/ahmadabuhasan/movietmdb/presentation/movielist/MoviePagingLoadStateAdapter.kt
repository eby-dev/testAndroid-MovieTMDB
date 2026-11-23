package com.ahmadabuhasan.movietmdb.presentation.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmadabuhasan.movietmdb.databinding.ItemLoadStateBinding

class MoviePagingLoadStateAdapter(
    private val onRetry: () -> Unit
) : LoadStateAdapter<MoviePagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.buttonRetry.setOnClickListener { onRetry() }
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: ItemLoadStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.textError.isVisible = loadState is LoadState.Error
            binding.buttonRetry.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                binding.textError.text = loadState.error.message ?: "Something went wrong."
            }
        }
    }
}
