package com.mbh.moviebrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.data.Movie
import com.mbh.moviebrowser.data.response.Genre
import com.mbh.moviebrowser.data.response.Result
import com.mbh.moviebrowser.repository.GenresRepository
import com.mbh.moviebrowser.repository.MoviesRepository
import com.mbh.moviebrowser.util.DispatcherProvider
import com.mbh.moviebrowser.util.IMAGE_BASE_URL
import com.mbh.moviebrowser.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val genresRepository: GenresRepository,
    private val moviesRepository: MoviesRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _movieListStateFlow = MutableStateFlow<List<Movie>>(emptyList())
    val movieListStateFlow = _movieListStateFlow.asStateFlow()

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading = _isLoading.asSharedFlow()

    private val _error = MutableStateFlow<Resource.Error?>(null)
    val error = _error.asSharedFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie = _selectedMovie.asStateFlow()

    init {
        getPopularMovies()
    }

    fun getPopularMovies() {
        viewModelScope.launch(dispatcherProvider.main) {
            moviesRepository.getPopularMovies().flowOn(dispatcherProvider.io)
                .combine(
                    genresRepository.getGenreList().flowOn(dispatcherProvider.io)
                ) { movieList, genreList ->
                    _isLoading.emit(movieList is Resource.Loading ||
                            genreList is Resource.Loading)
                    when {
                        movieList is Resource.Error -> _error.emit(movieList)
                        genreList is Resource.Error -> _error.emit(genreList)
                        movieList is Resource.Success && genreList is Resource.Success -> {
                            val movies = movieList.data?.let { resultList ->
                                genreList.data?.let { genres ->
                                    makeMovieList(resultList, genres)
                                }
                            }
                            movies?.let {
                                val currentList = _movieListStateFlow.value
                                _movieListStateFlow.emit(currentList + it)
                            }
                        }
                        else -> {}
                    }
                }.collect()
        }
    }

    fun clearError() {
        viewModelScope.launch(dispatcherProvider.main) {
            _error.emit(null)
        }
    }

    fun selectMovie(movie: Movie) {
        viewModelScope.launch(dispatcherProvider.main) {
            _selectedMovie.emit(movie)
        }
    }

    private fun makeMovieList(movieList: List<Result>, genreList: List<Genre>): List<Movie> {
        return movieList.map {
            Movie(
                id = it.id.toLong(),
                title = it.title,
                genres = mapGenre(it.genreIds, genreList),
                overview = it.overview,
                coverUrl = IMAGE_BASE_URL + it.posterPath,
                rating = it.voteAverage.toFloat(),
                isFavorite = false
            )
        }
    }

    private fun mapGenre(idList: List<Int>, genreList: List<Genre>): List<String> {
        return idList.mapNotNull { id ->
            genreList.find { it.id == id }?.name
        }
    }
}