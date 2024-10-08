package cl.moviedbapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cl.moviedbapp.viewmodel.MovieViewModel
import coil.compose.SubcomposeAsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieDetailView(movieId: String, viewModel: MovieViewModel, navController: NavController) {
    // Convierte el movieId de String a Int
    val selectedMovie = viewModel.getMovieById(movieId.toIntOrNull() ?: 0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(selectedMovie?.title ?: "Movie Detail", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Imagen destacada de la película (poster o backdrop)
            val imageUrl = "https://image.tmdb.org/t/p/w500/${selectedMovie?.posterPath ?: ""}"
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = selectedMovie?.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )

            // Contenedor de los detalles de la película
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shadow(1.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                // Título de la película
                Text(
                    text = selectedMovie?.title ?: "Unknown Title",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                Text(
                    text = selectedMovie?.overview ?: "No overview available.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    lineHeight = 20.sp,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Divider to separate sections
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(16.dp))

                // Fecha de lanzamiento y Calificación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Release Date:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = selectedMovie?.releaseDate ?: "Unknown",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Rating:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "⭐ ${selectedMovie?.voteAverage ?: 0.0}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to interact (e.g., share, add to favorites)
                Button(
                    onClick = { /* Action for a button, like sharing */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Add to Favorites")
                }
            }
        }
    }
}

