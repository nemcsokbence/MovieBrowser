package com.mbh.moviebrowser.viewmodel

import com.google.common.truth.Truth
import com.mbh.moviebrowser.data.Movie
import com.mbh.moviebrowser.util.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieDetailsViewModelTest {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    private val testMovie = Movie(
        id = 385687,
        title = "Fast X",
        genres = listOf("Action"),
        overview = "Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever.",
        coverUrl = "https://image.tmdb.org/t/p/w500/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
        rating = 7.4f,
        isFavorite = false,
    )

    @Before
    fun setUp() {
        movieDetailsViewModel = MovieDetailsViewModel(
            MutableStateFlow(testMovie).asStateFlow(),
            TestDispatchers()
        )
    }

    @Test
    fun `Favorite selection test`() = runBlocking {
        movieDetailsViewModel.onFavoriteClicked(true)

        Truth.assertThat(movieDetailsViewModel.isFavorite.first()).isEqualTo(true)
        Truth.assertThat(movieDetailsViewModel.selectedMovie.first()?.isFavorite).isEqualTo(true)
    }
}