package cl.moviedbapp.di

import android.content.Context
import androidx.room.Room
import cl.moviedbapp.data.MovieDatabase
import cl.moviedbapp.model.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {  // Asegúrate de usar @ApplicationContext
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movies.db"
        ).build()
    }

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
}
