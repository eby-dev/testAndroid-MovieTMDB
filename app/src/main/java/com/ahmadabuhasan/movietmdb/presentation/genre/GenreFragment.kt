package com.ahmadabuhasan.movietmdb.presentation.genre

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadabuhasan.movietmdb.R
import com.ahmadabuhasan.movietmdb.core.result.UiState
import com.ahmadabuhasan.movietmdb.databinding.FragmentGenreBinding
import com.ahmadabuhasan.movietmdb.domain.model.Genre
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GenreFragment : Fragment(R.layout.fragment_genre) {

    private var _binding: FragmentGenreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GenreViewModel by viewModels()

    private val adapter = GenreAdapter(onGenreClick = ::navigateToMovieList)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGenreBinding.bind(view)

        binding.recyclerGenres.adapter = adapter
        binding.recyclerGenres.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonRetry.setOnClickListener { viewModel.retry() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: UiState<List<Genre>>) {
        binding.progressBar.isVisible = state is UiState.Loading
        binding.layoutError.isVisible = state is UiState.Error
        binding.textEmpty.isVisible = state is UiState.Success && state.data.isEmpty()
        binding.recyclerGenres.isVisible = state is UiState.Success && state.data.isNotEmpty()

        when (state) {
            is UiState.Success -> adapter.submitList(state.data)
            is UiState.Error -> binding.textError.text = state.message
            else -> Unit
        }
    }

    private fun navigateToMovieList(genre: Genre) {
        findNavController().navigate(
            R.id.action_genreFragment_to_movieListFragment,
            bundleOf("genreId" to genre.id, "genreName" to genre.name)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerGenres.adapter = null
        _binding = null
    }
}
