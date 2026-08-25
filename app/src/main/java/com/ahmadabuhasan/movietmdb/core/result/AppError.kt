package com.ahmadabuhasan.movietmdb.core.result

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

sealed class AppError(message: String) : Exception(message) {
    data object NoInternet : AppError("No internet connection. Please check your network and try again.")
    data object Timeout : AppError("The request timed out. Please try again.")
    data class Http(val code: Int) : AppError("Something went wrong on the server (code $code).")
    data class Unknown(override val cause: Throwable) : AppError(cause.message ?: "Something went wrong.")
}

fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    is SocketTimeoutException -> AppError.Timeout
    is IOException -> AppError.NoInternet
    is HttpException -> AppError.Http(code())
    else -> AppError.Unknown(this)
}
