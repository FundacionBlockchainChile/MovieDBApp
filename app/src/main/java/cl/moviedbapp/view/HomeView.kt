package cl.moviedbapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cl.moviedbapp.model.MovieEntity
import cl.moviedbapp.viewmodel.MovieViewModel
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: MovieViewModel, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val moviePagingItems = viewModel.getMoviesPager().collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
                actions = {
                    // Botón para agregar una película
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.fetchMoviesFromApiAndSave()  // Función para obtener películas y guardarlas en la base de datos
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Movie")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(moviePagingItems) { movieEntity ->
                if (movieEntity != null) {
                    MovieCard(
                        movie = movieEntity,
                        onClick = { navController.navigate("movieDetail/${movieEntity.id}") },  // Para navegar a los detalles
                        onDeleteClick = {  // Para eliminar la película
                            coroutineScope.launch {
                                viewModel.deleteMovie(movieEntity)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: MovieEntity, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val imageUrl = "https://image.tmdb.org/t/p/w500/${movie.posterPath}"
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp), // Reducimos la altura de la imagen para darle más espacio al texto
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    ),
                    maxLines = 4,  // Ajustamos para mostrar más texto
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Release: ${movie.releaseDate}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        )
                    )

                    Text(
                        text = "⭐ ${movie.voteAverage}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Movie", tint = Color.Red)
                    }
                }
            }
        }
    }
}

