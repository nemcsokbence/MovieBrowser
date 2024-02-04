package com.mbh.moviebrowser.di

import com.mbh.moviebrowser.util.DefaultDispatchers
import com.mbh.moviebrowser.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @Singleton
    fun provideDefaultDispatchers(): DispatcherProvider {
        return DefaultDispatchers()
    }
}