package cl.moviedbapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.moviedbapp.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData
import cl.moviedbapp.data.MovieDataSource
import cl.moviedbapp.model.Movie

class MovieViewModel : ViewModel() {

    private val repository = MovieRepository()

    fun getMoviesPager(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 1, enablePlaceholders = false)) {
            MovieDataSource(repository)
        }.flow.cachedIn(viewModelScope)
    }

    private val _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList: StateFlow<List<Movie>> = _movieList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getMovieById(id: String): Movie? {
        return _movieList.value.find { it.id.toString() == id }
    }
}
