package com.mbh.moviebrowser.repository

import android.util.Log
import com.google.common.truth.Truth
import com.mbh.moviebrowser.api.TMDBApi
import com.mbh.moviebrowser.data.response.PopularMovieResponse
import com.mbh.moviebrowser.data.response.Result
import com.mbh.moviebrowser.util.Resource
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.net.UnknownHostException

class MoviesRepositoryTest {

    private lateinit var repository: MoviesRepository
    private val api: TMDBApi = mock()

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

    private val error401 = Response.error<PopularMovieResponse>(
        401, "".toResponseBody(null)
    )

    @Before
    fun setUp() {
        repository = MoviesRepositoryImpl(api)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `Get popular movies returns a list`() = runBlocking {
        whenever(api.getPopularMovieList()).thenReturn(
            Response.success(PopularMovieResponse(
                page = 1,
                totalPages = 1,
                totalResults = 1,
                results = mockMovieResponse
            ))
        )

        Truth.assertThat(repository.getPopularMovies().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getPopularMovies().drop(1).first().data?.first()
        ).isEqualTo(
            Resource.Success(mockMovieResponse).data?.first()
        )
    }

    @Test
    fun `Get popular movies returns a 401 error`() = runBlocking {
        whenever(api.getPopularMovieList()).thenReturn(
            error401
        )


        Truth.assertThat(repository.getPopularMovies().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getPopularMovies().drop(1).first()
        ).isEqualTo(
            Resource.Error.NetworkError(401)
        )
    }

    @Test
    fun `Api unknown host exception`() = runBlocking {
        whenever(api.getPopularMovieList()).doAnswer { throw UnknownHostException() }

        Truth.assertThat(repository.getPopularMovies().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getPopularMovies().drop(1).first()
        ).isEqualTo(
            Resource.Error.UnknownHostError
        )
    }

    @Test
    fun `Api unknown exception`() = runBlocking {
        whenever(api.getPopularMovieList()).doAnswer { throw Exception() }

        Truth.assertThat(repository.getPopularMovies().first()).isEqualTo(Resource.Loading)
        Truth.assertThat(
            repository.getPopularMovies().drop(1).first()
        ).isEqualTo(
            Resource.Error.UnknownError
        )
    }
}