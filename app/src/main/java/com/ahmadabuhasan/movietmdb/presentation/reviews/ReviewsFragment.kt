package com.ahmadabuhasan.movietmdb.presentation.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ahmadabuhasan.movietmdb.R
import com.ahmadabuhasan.movietmdb.databinding.FragmentReviewsBinding

// Placeholder destination proving Movie Detail -> Reviews navigation; real implementation lands in Phase 5.
class ReviewsFragment : Fragment(R.layout.fragment_reviews) {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewsBinding.bind(view)

        val movieId = arguments?.getInt("movieId") ?: -1
        binding.textPlaceholder.text = getString(R.string.reviews_placeholder, movieId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
