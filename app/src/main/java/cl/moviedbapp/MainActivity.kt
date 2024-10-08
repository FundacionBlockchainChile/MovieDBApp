package cl.moviedbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.moviedbapp.ui.theme.MovieDBAppTheme
import cl.moviedbapp.view.HomeView
import cl.moviedbapp.view.MovieDetailView
import cl.moviedbapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDBAppTheme {
                val navController = rememberNavController()
                val viewModel: MovieViewModel = hiltViewModel()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeView(viewModel, navController)
                    }
                    composable("movieDetail/{movieId}") { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")
                        if (movieId != null) {
                            MovieDetailView(movieId, viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}
