package com.mbh.moviebrowser.api

import com.mbh.moviebrowser.data.response.Genre
import com.mbh.moviebrowser.data.response.GenresResponse
import com.mbh.moviebrowser.data.response.PopularMovieResponse
import com.mbh.moviebrowser.data.response.Result
import retrofit2.Response

class MoviesApiMock : TMDBApi {

    private val mockMovieResponse = listOf(
        Result(
            adult = false,
            backdropPath = "",
            genreIds = listOf(1, 2),
            id = 455476,
            originalLanguage = "",
            title = "Knights of the Zodiac",
            originalTitle = "",
            overview = "When a headstrong street orphan, Seiya, in search of his abducted sister unwittingly taps into hidden powers, he discovers he might be the only person alive who can protect a reincarnated goddess, sent to watch over humanity. Can he let his past go and embrace his destiny to become a Knight of the Zodiac?",
            popularity = 0.0,
            posterPath = "/qW4crfED8mpNDadSmMdi7ZDzhXF.jpg",
            releaseDate = "",
            video = false,
            voteAverage = 6.5,
            voteCount = 1
        ),
        Result(
            adult = false,
            backdropPath = "",
            genreIds = listOf(1),
            id = 385687,
            originalLanguage = "",
            title = "Fast X",
            originalTitle = "",
            overview = "Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever.",
            popularity = 0.0,
            posterPath = "/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
            releaseDate = "",
            video = false,
            voteAverage = 7.4,
            voteCount = 1
        )
    )

    private val mockGenreResponse = listOf(
        Genre(1, "Action"),
        Genre(2, "Sci-fi")
    )

    override suspend fun getPopularMovieList(
        language: String?,
        page: Int?
    ): Response<PopularMovieResponse> {
        return Response.success(
            PopularMovieResponse(
                page = 1,
                totalPages = 1,
                totalResults = 1,
                results = List(5) {mockMovieResponse}.flatten()
            )
        )
    }

    override suspend fun getMovieGenreList(language: String?): Response<GenresResponse> {
        return Response.success(GenresResponse(mockGenreResponse))
    }
}