package cl.moviedbapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import cl.moviedbapp.model.Movie
import cl.moviedbapp.viewmodel.MovieViewModel
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: MovieViewModel, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val moviePagingItems = viewModel.getMoviesPager().collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Movies") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(moviePagingItems) { movie ->
                if (movie != null) {
                    MovieCard(movie) {
                        navController.navigate("movieDetail/${movie.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(8.dp) // Mayor elevación para resaltar la tarjeta
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Imagen de la película
            val imageUrl = "https://image.tmdb.org/t/p/w500/${movie.posterPath}"
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),  // Imagen destacada
                contentScale = ContentScale.Crop // Ajustar la imagen al tamaño sin deformar
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Contenedor para el título y detalles
            Column(modifier = Modifier.padding(16.dp)) {

                // Título de la película
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

                // Descripción (truncada si es muy larga)
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Detalles adicionales: Fecha y Calificación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fecha de lanzamiento
                    Text(
                        text = "Release: ${movie.releaseDate}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        )
                    )

                    // Calificación
                    Text(
                        text = "⭐ ${movie.voteAverage}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}


