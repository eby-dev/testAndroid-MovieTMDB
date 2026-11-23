package com.ahmadabuhasan.movietmdb.presentation.movielist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ahmadabuhasan.movietmdb.R
import com.ahmadabuhasan.movietmdb.databinding.FragmentMovieListBinding

// Placeholder destination proving Genre -> Movie List navigation; real implementation lands in Phase 3.
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieListBinding.bind(view)

        val genreId = arguments?.getInt("genreId") ?: -1
        val genreName = arguments?.getString("genreName").orEmpty()

        binding.textPlaceholder.text = getString(R.string.movie_list_placeholder, genreName, genreId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
