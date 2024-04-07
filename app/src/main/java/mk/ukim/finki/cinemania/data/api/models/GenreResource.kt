package mk.ukim.finki.cinemania.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResource(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
