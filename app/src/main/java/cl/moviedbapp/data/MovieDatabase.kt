package cl.moviedbapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.moviedbapp.model.MovieDao
import cl.moviedbapp.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
