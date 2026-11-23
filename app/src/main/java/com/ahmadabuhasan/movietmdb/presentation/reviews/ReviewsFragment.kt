package com.ahmadabuhasan.movietmdb.presentation.reviews

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadabuhasan.movietmdb.R
import com.ahmadabuhasan.movietmdb.databinding.FragmentReviewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewsFragment : Fragment(R.layout.fragment_reviews) {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewsViewModel by viewModels()

    private val reviewsAdapter = ReviewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewsBinding.bind(view)

        binding.toolbar.title = getString(R.string.reviews_screen_title)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        setupRecyclerView()
        binding.buttonRetry.setOnClickListener { reviewsAdapter.retry() }

        observeReviews()
        observeLoadState()
    }

    private fun setupRecyclerView() {
        val footerAdapter = ReviewPagingLoadStateAdapter { reviewsAdapter.retry() }
        binding.recyclerReviews.adapter = ConcatAdapter(reviewsAdapter, footerAdapter)
        binding.recyclerReviews.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeReviews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviews.collectLatest { pagingData ->
                    reviewsAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                reviewsAdapter.loadStateFlow.collectLatest { loadStates ->
                    val refresh = loadStates.refresh
                    val isEmpty = refresh is LoadState.NotLoading && reviewsAdapter.itemCount == 0

                    binding.progressBar.isVisible = refresh is LoadState.Loading
                    binding.layoutError.isVisible = refresh is LoadState.Error
                    binding.textEmpty.isVisible = isEmpty
                    binding.recyclerReviews.isVisible = refresh is LoadState.NotLoading && !isEmpty

                    if (refresh is LoadState.Error) {
                        binding.textError.text = refresh.error.message ?: "Something went wrong."
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerReviews.adapter = null
        _binding = null
    }
}
