package com.ahmadabuhasan.movietmdb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmadabuhasan.movietmdb.core.result.toAppError
import com.ahmadabuhasan.movietmdb.data.remote.api.TmdbApiService
import com.ahmadabuhasan.movietmdb.data.remote.dto.MovieDto
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1

class MoviePagingSource(
    private val api: TmdbApiService,
    private val genreId: Int
) : PagingSource<Int, MovieDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val page = params.key ?: STARTING_PAGE
        return try {
            val response = api.discoverMovies(genreId = genreId, page = page)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == STARTING_PAGE) null else page - 1,
                nextKey = if (page >= response.totalPages) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e.toAppError())
        } catch (e: HttpException) {
            LoadResult.Error(e.toAppError())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}
