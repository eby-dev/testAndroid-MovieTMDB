package com.ahmadabuhasan.movietmdb.presentation.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ahmadabuhasan.movietmdb.R
import com.ahmadabuhasan.movietmdb.core.result.UiState
import com.ahmadabuhasan.movietmdb.databinding.FragmentMovieDetailBinding
import com.ahmadabuhasan.movietmdb.domain.model.MovieDetail
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels()

    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)

        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        binding.buttonRetry.setOnClickListener { viewModel.retry() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: UiState<MovieDetail>) {
        binding.progressBar.isVisible = state is UiState.Loading
        binding.layoutError.isVisible = state is UiState.Error
        binding.scrollContent.isVisible = state is UiState.Success

        when (state) {
            is UiState.Success -> bindDetail(state.data)
            is UiState.Error -> binding.textError.text = state.message
            else -> Unit
        }
    }

    private fun bindDetail(detail: MovieDetail) {
        binding.textTitle.text = detail.title
        binding.textOverview.text = detail.overview.ifBlank { getString(R.string.movie_detail_no_overview) }
        binding.textGenres.text = detail.genres.joinToString { it.name }
            .ifBlank { getString(R.string.movie_detail_no_genres) }
        binding.textMeta.text = buildMetaText(detail)

        Glide.with(binding.imagePoster)
            .load(detail.posterUrl)
            .placeholder(R.drawable.placeholder_poster)
            .error(R.drawable.placeholder_poster)
            .centerCrop()
            .into(binding.imagePoster)

        Glide.with(binding.imageBackdrop)
            .load(detail.backdropUrl)
            .placeholder(R.drawable.placeholder_poster)
            .error(R.drawable.placeholder_poster)
            .centerCrop()
            .into(binding.imageBackdrop)

        binding.buttonTrailer.isVisible = detail.trailerKey != null
        binding.buttonTrailer.setOnClickListener { detail.trailerKey?.let(::openTrailer) }

        binding.buttonReviews.setOnClickListener { navigateToReviews(detail.id) }
    }

    private fun buildMetaText(detail: MovieDetail): String {
        val parts = mutableListOf(formatReleaseDate(detail.releaseDate))
        detail.runtimeMinutes?.let { parts.add(getString(R.string.movie_detail_runtime_minutes, it)) }
        parts.add(getString(R.string.movie_detail_rating_format, detail.voteAverage))
        return parts.joinToString(" • ")
    }

    private fun openTrailer(trailerKey: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$trailerKey"))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), R.string.movie_detail_trailer_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToReviews(movieId: Int) {
        findNavController().navigate(
            R.id.action_movieDetailFragment_to_reviewsFragment,
            bundleOf("movieId" to movieId)
        )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
