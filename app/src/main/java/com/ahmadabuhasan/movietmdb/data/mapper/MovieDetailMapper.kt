package com.ahmadabuhasan.movietmdb.data.mapper

import com.ahmadabuhasan.movietmdb.data.remote.dto.MovieDetailDto
import com.ahmadabuhasan.movietmdb.data.remote.dto.VideoDto
import com.ahmadabuhasan.movietmdb.domain.model.MovieDetail

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
private const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"

fun MovieDetailDto.toDomain(): MovieDetail = MovieDetail(
    id = id,
    title = title.orEmpty().ifBlank { "Untitled" },
    overview = overview.orEmpty(),
    posterUrl = posterPath?.let { POSTER_BASE_URL + it },
    backdropUrl = backdropPath?.let { BACKDROP_BASE_URL + it },
    releaseDate = releaseDate.orEmpty(),
    voteAverage = voteAverage ?: 0.0,
    runtimeMinutes = runtime,
    genres = genres.orEmpty().toDomain(),
    trailerKey = videos?.results.orEmpty().findTrailerKey()
)

// UI only ever needs "is there a trailer" (trailerKey null or not) - the YouTube/Trailer/official
// filtering rules live here so MovieDetailFragment never has to know about TMDB's video-type taxonomy.
private fun List<VideoDto>.findTrailerKey(): String? {
    val youtubeTrailers = filter { it.site == "YouTube" && it.type == "Trailer" }
    return (youtubeTrailers.firstOrNull { it.official == true } ?: youtubeTrailers.firstOrNull())?.key
}
