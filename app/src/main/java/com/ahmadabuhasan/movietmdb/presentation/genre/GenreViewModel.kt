package com.ahmadabuhasan.movietmdb.presentation.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmadabuhasan.movietmdb.core.result.AppError
import com.ahmadabuhasan.movietmdb.core.result.UiState
import com.ahmadabuhasan.movietmdb.domain.model.Genre
import com.ahmadabuhasan.movietmdb.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Genre>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Genre>>> = _uiState.asStateFlow()

    init {
        loadGenres()
    }

    fun retry() = loadGenres()

    private fun loadGenres() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(repository.getGenres())
            } catch (e: AppError) {
                UiState.Error(e.message ?: "Something went wrong.")
            }
        }
    }
}
