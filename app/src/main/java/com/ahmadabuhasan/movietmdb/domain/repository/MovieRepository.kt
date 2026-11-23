package com.ahmadabuhasan.movietmdb.domain.repository

import androidx.paging.PagingData
import com.ahmadabuhasan.movietmdb.domain.model.Genre
import com.ahmadabuhasan.movietmdb.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getGenres(): List<Genre>
    fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>>
}
