package com.mbh.moviebrowser.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mbh.moviebrowser.data.response.Genre

@Dao
interface GenreDao {
    @Upsert
    suspend fun upsert(genre: Genre): Long
    @Query("SELECT * FROM genres")
    fun getGenres(): List<Genre>?
}