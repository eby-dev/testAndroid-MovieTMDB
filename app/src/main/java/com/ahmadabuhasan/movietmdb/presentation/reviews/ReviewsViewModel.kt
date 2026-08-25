package com.ahmadabuhasan.movietmdb.presentation.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmadabuhasan.movietmdb.domain.model.Review
import com.ahmadabuhasan.movietmdb.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "ReviewsFragment requires a movieId argument"
    }

    val reviews: Flow<PagingData<Review>> = repository.getReviews(movieId)
        .cachedIn(viewModelScope)
}
