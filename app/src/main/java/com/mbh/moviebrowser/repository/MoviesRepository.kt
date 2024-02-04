package com.mbh.moviebrowser.repository

import android.util.Log
import com.mbh.moviebrowser.api.TMDBApi
import com.mbh.moviebrowser.data.response.Result
import com.mbh.moviebrowser.util.ErrorHandler.getMovieBrowserError
import com.mbh.moviebrowser.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface MoviesRepository {
    suspend fun getPopularMovies(
        language: String? = "en-US"
    ): Flow<Resource<List<Result>>>
}

class MoviesRepositoryImpl @Inject constructor(private val tmdbApi: TMDBApi) : MoviesRepository {

    private var page = 1
    override suspend fun getPopularMovies(language: String?): Flow<Resource<List<Result>>> = flow {
        emit(Resource.Loading)
        val response = tmdbApi.getPopularMovieList(language, page)
        if (response.isSuccessful) {
            page++
            response.body()?.results?.let { emit(Resource.Success(it)) }
        } else {
            Log.e(TAG, response.code().toString())
            emit(Resource.Error.NetworkError(response.code()))
        }
    }.catch {
        emit(it.getMovieBrowserError())
    }
}