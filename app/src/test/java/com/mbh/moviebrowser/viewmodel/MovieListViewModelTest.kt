package com.mbh.moviebrowser.viewmodel

import com.google.common.truth.Truth.assertThat
import com.mbh.moviebrowser.data.Movie
import com.mbh.moviebrowser.data.response.Genre
import com.mbh.moviebrowser.data.response.Result
import com.mbh.moviebrowser.repository.GenresRepository
import com.mbh.moviebrowser.repository.MoviesRepository
import com.mbh.moviebrowser.util.Resource
import com.mbh.moviebrowser.util.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class MovieListViewModelTest {

    private val genresRepository: GenresRepository = mock()

    private val moviesRepository: MoviesRepository = mock()


    private lateinit var movieListViewModel: MovieListViewModel

    private val testMovies = listOf(
        Movie(
            id = 455476,
            title = "Knights of the Zodiac",
            genres = listOf("Action", "Sci-fi"),
            overview = "When a headstrong street orphan, Seiya, in search of his abducted sister unwittingly taps into hidden powers, he discovers he might be the only person alive who can protect a reincarnated goddess, sent to watch over humanity. Can he let his past go and embrace his destiny to become a Knight of the Zodiac?",
            coverUrl = "https://image.tmdb.org/t/p/w500/qW4crfED8mpNDadSmMdi7ZDzhXF.jpg",
            rating = 6.5f,
            isFavorite = false,
        ),
        Movie(
            id = 385687,
            title = "Fast X",
            genres = listOf("Action"),
            overview = "Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever.",
            coverUrl = "https://image.tmdb.org/t/p/w500/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
            rating = 7.4f,
            isFavorite = false,
        ),
    )

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

    @Before
    fun setUp() {
        movieListViewModel = MovieListViewModel(
            genresRepository,
            moviesRepository,
            TestDispatchers()
        )
    }

    @Test
    fun `Get movies returns a list`() = runBlocking {
        whenever(genresRepository.getGenreList()).thenReturn(
            flowOf(Resource.Success(mockGenreResponse))
        )

        whenever(moviesRepository.getPopularMovies()).thenReturn(
            flowOf(Resource.Success(mockMovieResponse))
        )

        movieListViewModel.getPopularMovies()
        assertThat(movieListViewModel.movieListStateFlow.first()).isEqualTo(testMovies)
    }

    @Test
    fun `Get movies on error`() = runBlocking {
        whenever(moviesRepository.getPopularMovies()).thenReturn(
            flowOf(Resource.Success(mockMovieResponse))
        )

        whenever(genresRepository.getGenreList()).thenReturn(
            flowOf(Resource.Error.UnknownError)
        )

        movieListViewModel.getPopularMovies()

        assertThat(movieListViewModel.error.first()).isEqualTo(Resource.Error.UnknownError)
    }

    @Test
    fun `Clear error method makes the error null`() = runBlocking {
        movieListViewModel.clearError()

        assertThat(movieListViewModel.error.first()).isEqualTo(null)
    }

    @Test
    fun `Select movie test`() = runBlocking {
        movieListViewModel.selectMovie(testMovies.first())

        assertThat(movieListViewModel.selectedMovie.first()).isEqualTo(testMovies.first())
    }
}
