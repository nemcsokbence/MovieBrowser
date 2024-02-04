package com.mbh.moviebrowser.data

data class Movie(
    val id: Long,
    val title: String,
    val genres: List<String>,
    val overview: String?,
    val coverUrl: String?,
    val rating: Float,
    var isFavorite: Boolean,
)
