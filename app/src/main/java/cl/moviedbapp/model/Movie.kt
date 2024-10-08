package cl.moviedbapp.model
import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,  // Mapea con poster_path del JSON

    @SerializedName("release_date")
    val releaseDate: String?,  // Mapea con release_date del JSON

    @SerializedName("vote_average")
    val voteAverage: Double  // Mapea con vote_average del JSON
)

data class MovieResponse(
    val results: List<Movie>
)
