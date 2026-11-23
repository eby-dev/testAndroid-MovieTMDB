package com.ahmadabuhasan.movietmdb.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<ReviewDto>,
    @SerializedName("total_pages") val totalPages: Int
)

data class ReviewDto(
    @SerializedName("id") val id: String,
    @SerializedName("author") val author: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("author_details") val authorDetails: AuthorDetailsDto?
)

data class AuthorDetailsDto(
    @SerializedName("rating") val rating: Double?
)
