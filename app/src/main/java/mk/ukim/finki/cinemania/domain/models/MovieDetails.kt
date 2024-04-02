package mk.ukim.finki.cinemania.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterImage: String?,
    val releaseDate: String,
    val rating: Double,
    val runtime: Int,
    val genres: List<String>,
    val productionCountries: List<String>,
    val spokenLanguages: List<String>
)
