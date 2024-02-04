package com.mbh.moviebrowser.di

import android.content.Context
import androidx.room.Room
import com.mbh.moviebrowser.db.GenreDao
import com.mbh.moviebrowser.db.GenreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): GenreDatabase {
        return Room.databaseBuilder(
            appContext,
            GenreDatabase::class.java,
            "genre.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMessagesDao(genreDatabase: GenreDatabase): GenreDao {
        return genreDatabase.getGenreDao()
    }
}