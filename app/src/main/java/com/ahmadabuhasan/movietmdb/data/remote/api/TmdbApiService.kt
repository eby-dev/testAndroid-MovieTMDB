package com.ahmadabuhasan.movietmdb.data.remote.api

import com.ahmadabuhasan.movietmdb.data.remote.dto.GenreListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "en-US"
    ): GenreListResponse
}
