package com.ahmadabuhasan.movietmdb.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val runtimeMinutes: Int?,
    val genres: List<Genre>,
    val trailerKey: String?
)
