package mk.ukim.finki.cinemania.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResource(
    @SerialName("title")
    val title: String,
    @SerialName("poster_path")
    val posterPath: String? = null
)