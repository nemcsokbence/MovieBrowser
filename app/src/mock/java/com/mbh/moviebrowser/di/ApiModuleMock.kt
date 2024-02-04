package com.mbh.moviebrowser.di

import com.mbh.moviebrowser.api.MoviesApiMock
import com.mbh.moviebrowser.api.TMDBApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModuleMock {

    @Provides
    @Singleton
    fun provideMockMoviesApi(): TMDBApi {
        return MoviesApiMock()
    }
}