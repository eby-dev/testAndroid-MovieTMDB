package com.ahmadabuhasan.movietmdb.domain.repository

import com.ahmadabuhasan.movietmdb.domain.model.Genre

interface MovieRepository {
    suspend fun getGenres(): List<Genre>
}
