package cl.moviedbapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingSource
import androidx.room.Delete

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY title ASC")
    fun getMoviesPagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies ORDER BY title ASC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
}
