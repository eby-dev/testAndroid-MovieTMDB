package com.ahmadabuhasan.movietmdb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ahmadabuhasan.movietmdb.core.result.toAppError
import com.ahmadabuhasan.movietmdb.data.mapper.toDomain
import com.ahmadabuhasan.movietmdb.data.paging.MoviePagingSource
import com.ahmadabuhasan.movietmdb.data.paging.ReviewPagingSource
import com.ahmadabuhasan.movietmdb.data.remote.api.TmdbApiService
import com.ahmadabuhasan.movietmdb.domain.model.Genre
import com.ahmadabuhasan.movietmdb.domain.model.Movie
import com.ahmadabuhasan.movietmdb.domain.model.MovieDetail
import com.ahmadabuhasan.movietmdb.domain.model.Review
import com.ahmadabuhasan.movietmdb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val MOVIE_PAGE_SIZE = 20
private const val REVIEW_PAGE_SIZE = 20

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

    override fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = MOVIE_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(api, genreId) }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetail {
        try {
            return api.getMovieDetail(movieId).toDomain()
        } catch (e: IOException) {
            throw e.toAppError()
        } catch (e: HttpException) {
            throw e.toAppError()
        }
    }

    override fun getReviews(movieId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(pageSize = REVIEW_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ReviewPagingSource(api, movieId) }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }
}
