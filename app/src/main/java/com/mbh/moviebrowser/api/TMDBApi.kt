package com.mbh.moviebrowser.api

import com.mbh.moviebrowser.data.response.GenresResponse
import com.mbh.moviebrowser.data.response.PopularMovieResponse
import com.mbh.moviebrowser.util.GENRES_ENDPOINT
import com.mbh.moviebrowser.util.MOVIES_ENDPOINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET("${MOVIES_ENDPOINT}/popular")
    suspend fun getPopularMovieList(
        @Query("language")
        language: String? = "en-US",
        @Query("page")
        page: Int? = 1
    ): Response<PopularMovieResponse>

    @GET("${GENRES_ENDPOINT}/movie/list")
    suspend fun getMovieGenreList(
        @Query("language")
        language: String? = "en-US",
    ): Response<GenresResponse>
}