package com.ahmadabuhasan.movietmdb.di

import com.ahmadabuhasan.movietmdb.data.repository.MovieRepositoryImpl
import com.ahmadabuhasan.movietmdb.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository
}
