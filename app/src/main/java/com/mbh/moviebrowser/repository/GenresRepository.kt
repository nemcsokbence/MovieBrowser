package com.mbh.moviebrowser.repository

import com.mbh.moviebrowser.api.TMDBApi
import com.mbh.moviebrowser.data.response.Genre
import com.mbh.moviebrowser.db.DataStoreManager
import com.mbh.moviebrowser.db.GenreDao
import com.mbh.moviebrowser.util.ErrorHandler.getMovieBrowserError
import com.mbh.moviebrowser.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val TAG = "Repository"

interface GenresRepository {
    suspend fun getGenreList(): Flow<Resource<List<Genre>>>
}

class GenresRepositoryImpl @Inject constructor(
    private val tmdbApi: TMDBApi,
    private val genreDao: GenreDao,
    private val dataStoreManager: DataStoreManager
) : GenresRepository {
    override suspend fun getGenreList(): Flow<Resource<List<Genre>>> = flow {
        emit(Resource.Loading)
        val cachedGenres = genreDao.getGenres()
        if (isCacheUpToDate() && !cachedGenres.isNullOrEmpty()) {
            emit(Resource.Success(cachedGenres))
        } else {
            val response = tmdbApi.getMovieGenreList()
            if (response.isSuccessful) {
                response.body()?.genres?.let { saveGenresToDB(it) }
                emit(Resource.Success(genreDao.getGenres()))
                saveLastUpdate()
            } else {
                emit(Resource.Error.NetworkError(response.code()))
            }
        }
    }.catch {
        emit(it.getMovieBrowserError())
    }

    private suspend fun saveGenresToDB(genres: List<Genre>) {
        for (genre in genres) {
            genreDao.upsert(genre)
        }
    }

    private suspend fun isCacheUpToDate(): Boolean {
        val timestamp = dataStoreManager.getLastSyncTimestamp()
        return if (timestamp != null) {
            val currentTimestamp = System.currentTimeMillis()
            val durationMillis = currentTimestamp - timestamp
            val oneDayMillis = TimeUnit.DAYS.toMillis(1)
            durationMillis < oneDayMillis
        } else false
    }

    private suspend fun saveLastUpdate() {
        dataStoreManager.saveLastSyncTimestamp(System.currentTimeMillis())
    }
}