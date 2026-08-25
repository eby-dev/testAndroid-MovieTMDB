package com.ahmadabuhasan.movietmdb.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val releaseDate: String,
    val voteAverage: Double
)
