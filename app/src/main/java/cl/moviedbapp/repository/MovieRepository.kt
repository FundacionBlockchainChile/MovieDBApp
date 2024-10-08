package cl.moviedbapp.repository

import cl.moviedbapp.model.MovieResponse
import cl.moviedbapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import cl.moviedbapp.model.MovieDao
import cl.moviedbapp.model.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import androidx.paging.PagingSource

interface MovieApi {
    @GET("discover/movie")
    suspend fun getMoviesPaged(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): MovieResponse
}

class MovieRepository @Inject constructor(private val movieDao: MovieDao) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(MovieApi::class.java)

    suspend fun getMoviesPaged(page: Int): MovieResponse {
        return api.getMoviesPaged(page)
    }

    suspend fun insertMovies(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
    }

    fun getAllMoviesFromDb(): Flow<List<MovieEntity>> {
        return movieDao.getAllMovies()
    }

    fun getPagingSource(): PagingSource<Int, MovieEntity> {
        return movieDao.getMoviesPagingSource()  // Asegúrate de que el DAO tenga esta función implementada
    }

    suspend fun deleteMovie(movie: MovieEntity) {
        movieDao.deleteMovie(movie)
    }
}
