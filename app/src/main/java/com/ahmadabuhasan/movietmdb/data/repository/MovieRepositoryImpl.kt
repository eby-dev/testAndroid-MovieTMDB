package com.ahmadabuhasan.movietmdb.data.repository

import com.ahmadabuhasan.movietmdb.core.result.toAppError
import com.ahmadabuhasan.movietmdb.data.mapper.toDomain
import com.ahmadabuhasan.movietmdb.data.remote.api.TmdbApiService
import com.ahmadabuhasan.movietmdb.domain.model.Genre
import com.ahmadabuhasan.movietmdb.domain.repository.MovieRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApiService
) : MovieRepository {

    override suspend fun getGenres(): List<Genre> {
        try {
            return api.getGenres().genres.toDomain()
        } catch (e: IOException) {
            throw e.toAppError()
        } catch (e: HttpException) {
            throw e.toAppError()
        }
    }
}
