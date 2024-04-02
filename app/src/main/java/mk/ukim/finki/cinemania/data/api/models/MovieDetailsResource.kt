package mk.ukim.finki.cinemania.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResource(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("genres")
    val genres: List<GenreResource>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountryResource>,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguageResource>,
    @SerialName("runtime")
    val runtime: Int
)
