package com.ahmadabuhasan.movietmdb.presentation.movielist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.ahmadabuhasan.movietmdb.R
import com.ahmadabuhasan.movietmdb.databinding.FragmentMovieListBinding
import com.ahmadabuhasan.movietmdb.domain.model.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val SPAN_COUNT = 2

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels()

    private val movieAdapter = MovieListAdapter(onMovieClick = ::navigateToMovieDetail)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieListBinding.bind(view)

        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = getString(R.string.movie_list_screen_title)

        setupRecyclerView()
        binding.buttonRetry.setOnClickListener { movieAdapter.retry() }

        observeMovies()
        observeLoadState()
    }

    private fun setupRecyclerView() {
        val footerAdapter = MoviePagingLoadStateAdapter { movieAdapter.retry() }
        binding.recyclerMovies.adapter = ConcatAdapter(movieAdapter, footerAdapter)

        val layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                if (position >= movieAdapter.itemCount) SPAN_COUNT else 1
        }
        binding.recyclerMovies.layoutManager = layoutManager
    }

    private fun observeMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collectLatest { pagingData ->
                    movieAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieAdapter.loadStateFlow.collectLatest { loadStates ->
                    val refresh = loadStates.refresh
                    val isEmpty = refresh is LoadState.NotLoading && movieAdapter.itemCount == 0

                    binding.progressBar.isVisible = refresh is LoadState.Loading
                    binding.layoutError.isVisible = refresh is LoadState.Error
                    binding.textEmpty.isVisible = isEmpty
                    binding.recyclerMovies.isVisible = refresh is LoadState.NotLoading && !isEmpty

                    if (refresh is LoadState.Error) {
                        binding.textError.text = refresh.error.message ?: "Something went wrong."
                    }
                }
            }
        }
    }

    private fun navigateToMovieDetail(movie: Movie) {
        findNavController().navigate(
            R.id.action_movieListFragment_to_movieDetailFragment,
            bundleOf("movieId" to movie.id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerMovies.adapter = null
        _binding = null
    }
}
