package com.ahmadabuhasan.movietmdb.domain.model

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val createdDate: String,
    val rating: Double?
)
