package cl.moviedbapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.moviedbapp.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import cl.moviedbapp.model.MovieEntity
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieList = MutableStateFlow<List<MovieEntity>>(emptyList())
    val movieList: StateFlow<List<MovieEntity>> = _movieList

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            repository.getAllMoviesFromDb()
                .onEach { movies ->
                    if (movies.isEmpty()) {
                        fetchMoviesFromApiAndSave()
                    } else {
                        _movieList.value = movies
                    }
                }
                .launchIn(this)
        }
    }

    // Función para obtener películas desde la API y guardarlas en la base de datos
    fun fetchMoviesFromApiAndSave() {
        viewModelScope.launch {
            val response = repository.getMoviesPaged(1)
            val movieEntities = response.results.map { movie ->
                MovieEntity(
                    movie.id,
                    movie.title,
                    movie.overview,
                    movie.posterPath,
                    movie.releaseDate,
                    movie.voteAverage
                )
            }
            repository.insertMovies(movieEntities)
        }
    }

    fun getMoviesPager(): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { repository.getPagingSource() }
        ).flow.cachedIn(viewModelScope)
    }

    fun getMovieById(id: Int): MovieEntity? {
        return _movieList.value.find { it.id == id }
    }

    // Función para eliminar una película
    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }
}
