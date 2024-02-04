package com.mbh.moviebrowser.data.response

import com.google.gson.annotations.SerializedName

data class PopularMovieResponse(
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)