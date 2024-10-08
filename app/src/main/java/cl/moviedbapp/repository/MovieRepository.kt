package cl.moviedbapp.repository

import cl.moviedbapp.model.MovieResponse
import cl.moviedbapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie")
    suspend fun getMoviesPaged(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): MovieResponse
}

class MovieRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(MovieApi::class.java)

    suspend fun getMoviesPaged(page: Int) = api.getMoviesPaged(page)
}
