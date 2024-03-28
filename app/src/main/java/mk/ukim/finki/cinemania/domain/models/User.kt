package mk.ukim.finki.cinemania.domain.models

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String? = null,
    val photoUrl: String? = null
)