package com.mbh.moviebrowser.di

import com.mbh.moviebrowser.api.TMDBApi
import com.mbh.moviebrowser.db.DataStoreManager
import com.mbh.moviebrowser.db.GenreDao
import com.mbh.moviebrowser.repository.GenresRepository
import com.mbh.moviebrowser.repository.GenresRepositoryImpl
import com.mbh.moviebrowser.repository.MoviesRepository
import com.mbh.moviebrowser.repository.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(tmdbApi: TMDBApi): MoviesRepository {
        return MoviesRepositoryImpl(tmdbApi)
    }

    @Provides
    @Singleton
    fun provideGenresRepository(
        tmdbApi: TMDBApi,
        dao: GenreDao,
        dataStoreManager: DataStoreManager
    ): GenresRepository {
        return GenresRepositoryImpl(tmdbApi, dao, dataStoreManager)
    }

}