package cl.moviedbapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.moviedbapp.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieDetailView(movieId: String, viewModel: MovieViewModel, navController: NavController) {
    val selectedMovie = viewModel.getMovieById(movieId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedMovie?.title ?: "Movie Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(selectedMovie?.overview ?: "", style = MaterialTheme.typography.bodyMedium)
            Text("Release date: ${selectedMovie?.releaseDate}", style = MaterialTheme.typography.bodySmall)
            Text("Rating: ${selectedMovie?.voteAverage}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
