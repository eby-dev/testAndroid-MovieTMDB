package com.ahmadabuhasan.movietmdb.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmadabuhasan.movietmdb.core.result.AppError
import com.ahmadabuhasan.movietmdb.core.result.UiState
import com.ahmadabuhasan.movietmdb.domain.model.MovieDetail
import com.ahmadabuhasan.movietmdb.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "MovieDetailFragment requires a movieId argument"
    }

    private val _uiState = MutableStateFlow<UiState<MovieDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<MovieDetail>> = _uiState.asStateFlow()

    init {
        loadMovieDetail()
    }

    fun retry() = loadMovieDetail()

    private fun loadMovieDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(repository.getMovieDetail(movieId))
            } catch (e: AppError) {
                UiState.Error(e.message ?: "Something went wrong.")
            }
        }
    }
}
