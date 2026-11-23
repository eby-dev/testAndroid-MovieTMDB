package com.ahmadabuhasan.movietmdb.presentation.movielist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmadabuhasan.movietmdb.domain.model.Movie
import com.ahmadabuhasan.movietmdb.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val genreId: Int = checkNotNull(savedStateHandle["genreId"]) {
        "MovieListFragment requires a genreId argument"
    }

    val genreName: String = savedStateHandle["genreName"] ?: ""

    val movies: Flow<PagingData<Movie>> = repository.getMoviesByGenre(genreId)
        .cachedIn(viewModelScope)
}
