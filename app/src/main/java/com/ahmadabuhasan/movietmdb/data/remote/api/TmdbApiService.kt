package com.ahmadabuhasan.movietmdb.data.remote.api

import com.ahmadabuhasan.movietmdb.data.remote.dto.GenreListResponse
import com.ahmadabuhasan.movietmdb.data.remote.dto.MovieDetailDto
import com.ahmadabuhasan.movietmdb.data.remote.dto.MovieListResponse
import com.ahmadabuhasan.movietmdb.data.remote.dto.ReviewListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "en-US"
    ): GenreListResponse

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("language") language: String = "en-US"
    ): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = "videos",
        @Query("language") language: String = "en-US"
    ): MovieDetailDto

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): ReviewListResponse
}
