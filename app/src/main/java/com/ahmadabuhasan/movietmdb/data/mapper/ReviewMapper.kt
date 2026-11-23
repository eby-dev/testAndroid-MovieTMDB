package com.ahmadabuhasan.movietmdb.data.mapper

import com.ahmadabuhasan.movietmdb.data.remote.dto.ReviewDto
import com.ahmadabuhasan.movietmdb.domain.model.Review

fun ReviewDto.toDomain(): Review = Review(
    id = id,
    author = author.orEmpty().ifBlank { "Anonymous" },
    content = content.orEmpty().ifBlank { "No review content." },
    createdDate = createdAt.orEmpty(),
    rating = authorDetails?.rating
)
