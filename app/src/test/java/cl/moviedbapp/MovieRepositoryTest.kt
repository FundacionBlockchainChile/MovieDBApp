package cl.moviedbapp

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cl.moviedbapp.model.MovieEntity
import cl.moviedbapp.repository.MovieRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var movieRepository: MovieRepository

    private val movie1 = MovieEntity(1, "Inception", "A mind-bending thriller", "/inception.jpg", "2010-07-16", 8.8)
    private val movie2 = MovieEntity(2, "The Dark Knight", "A dark, thrilling movie", "/darkknight.jpg", "2008-07-18", 9.0)

    @Before
    fun setUp() {
        // Configurar el MockWebServer
        mockWebServer = MockWebServer()

        // Crear la instancia del API simulado
        val movieApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(cl.moviedbapp.repository.MovieApi::class.java)

        // Crear el repositorio con el DAO simulado
        movieRepository = MovieRepository(MockMovieDao())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchMoviesFromApiAndSave saves movies to the database`() = runBlocking {
        // Simular una respuesta de la API
        val mockResponse = MockResponse()
        mockResponse.setBody("""
        {
            "results": [
                {
                    "id": 1,
                    "title": "Inception",
                    "overview": "A mind-bending thriller",
                    "poster_path": "/inception.jpg",
                    "release_date": "2010-07-16",
                    "vote_average": 8.8
                },
                {
                    "id": 2,
                    "title": "The Dark Knight",
                    "overview": "A dark, thrilling movie",
                    "poster_path": "/darkknight.jpg",
                    "release_date": "2008-07-18",
                    "vote_average": 9.0
                }
            ]
        }
    """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // Ejecutar la función que obtiene películas desde la API y las guarda
        movieRepository.getMoviesPaged(1)

        // Verificar que el servidor haya recibido la solicitud correcta
        val request = mockWebServer.takeRequest()
        println("Request sent: ${request.path}")  // Log de la solicitud enviada

        // Verificar que las películas se hayan guardado correctamente
        val movies = movieRepository.getAllMoviesFromDb().first()
        println("Movies stored in DB: ${movies.size}")  // Log para ver cuántas películas hay en la DB
        assertEquals(2, movies.size)
        assertEquals("Inception", movies[0].title)
        assertEquals("The Dark Knight", movies[1].title)
    }


    @Test
    fun `deleteMovie removes movie from database`() = runBlocking {
        // Agregar películas a la base de datos
        movieRepository.insertMovies(listOf(movie1, movie2))
        movieRepository.deleteMovie(movie1)

        // Verificar que solo quede una película
        val movies = movieRepository.getAllMoviesFromDb().first()
        assertEquals(1, movies.size)
        assertEquals("The Dark Knight", movies[0].title)
    }

    // Simulación del MovieDao
    class MockMovieDao : cl.moviedbapp.model.MovieDao {
        private val moviesFlow = MutableStateFlow<List<MovieEntity>>(emptyList())

        override suspend fun insertMovies(movies: List<MovieEntity>) {
            moviesFlow.value = moviesFlow.value.toMutableList().apply { addAll(movies) }
        }

        override fun getMoviesPagingSource(): PagingSource<Int, MovieEntity> {
            return object : PagingSource<Int, MovieEntity>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
                    return LoadResult.Page(
                        data = moviesFlow.value,
                        prevKey = null,
                        nextKey = null
                    )
                }

                override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
                    return null
                }
            }
        }

        override fun getAllMovies() = moviesFlow

        override suspend fun deleteMovie(movie: MovieEntity) {
            moviesFlow.value = moviesFlow.value.toMutableList().apply { remove(movie) }
        }
    }
}
