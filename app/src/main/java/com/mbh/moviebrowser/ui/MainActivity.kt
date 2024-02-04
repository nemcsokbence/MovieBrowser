package com.mbh.moviebrowser.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mbh.moviebrowser.util.DefaultDispatchers
import com.mbh.moviebrowser.viewmodel.MovieDetailsViewModel
import com.mbh.moviebrowser.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val isLoading = viewModel.isLoading.collectAsState(initial = false)
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        MovieListScreen(
                            viewModel = viewModel,
                            onDetailsClicked = {
                                navController.navigate("details")
                            },
                        )
                    }
                    composable("details") {
                        MovieDetailsScreen(
                            viewModel = MovieDetailsViewModel(
                                viewModel.selectedMovie,
                                DefaultDispatchers()
                            ),
                        )
                    }
                }
                MovieAlertDialog(error = viewModel.error) {
                    viewModel.clearError()
                }
                if (isLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        FullScreenLoader()
                    }
                }
            }
        }
    }
}
