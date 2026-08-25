package com.ahmadabuhasan.movietmdb.data.mapper

import com.ahmadabuhasan.movietmdb.data.remote.dto.GenreDto
import com.ahmadabuhasan.movietmdb.domain.model.Genre

fun GenreDto.toDomain(): Genre = Genre(id = id, name = name)

fun List<GenreDto>.toDomain(): List<Genre> = map { it.toDomain() }
