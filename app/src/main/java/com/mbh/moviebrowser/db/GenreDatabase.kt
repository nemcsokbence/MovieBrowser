package com.mbh.moviebrowser.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mbh.moviebrowser.data.response.Genre

@Database(
    entities = [Genre::class],
    version = 1
)
abstract class GenreDatabase: RoomDatabase() {
    abstract fun getGenreDao(): GenreDao
}