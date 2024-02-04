package com.mbh.moviebrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.data.Movie
import com.mbh.moviebrowser.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    val selectedMovie: StateFlow<Movie?>,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(selectedMovie.value?.isFavorite ?: false)
    val isFavorite = _isFavorite.asStateFlow()

    init {
        viewModelScope.launch(dispatcherProvider.main) {
            isFavorite.collect {
                selectedMovie.value?.isFavorite = it
            }
        }

    }

    fun onFavoriteClicked(isFavorite: Boolean) {
        viewModelScope.launch(dispatcherProvider.main)  {
            _isFavorite.emit(isFavorite)
        }
    }
}
