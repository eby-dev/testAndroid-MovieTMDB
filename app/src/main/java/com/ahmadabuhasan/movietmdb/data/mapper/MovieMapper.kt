package com.ahmadabuhasan.movietmdb.data.mapper

import com.ahmadabuhasan.movietmdb.data.remote.dto.MovieDto
import com.ahmadabuhasan.movietmdb.domain.model.Movie

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title.orEmpty().ifBlank { "Untitled" },
    posterUrl = posterPath?.let { POSTER_BASE_URL + it },
    releaseDate = releaseDate.orEmpty(),
    voteAverage = voteAverage ?: 0.0
)
