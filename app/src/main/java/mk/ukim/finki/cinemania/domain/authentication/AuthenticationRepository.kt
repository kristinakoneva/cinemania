package mk.ukim.finki.cinemania.domain.authentication

import mk.ukim.finki.cinemania.domain.models.User

interface AuthenticationRepository {

    suspend fun registerUser(email: String, password: String): Boolean

    suspend fun loginUser(email: String, password: String): Boolean

    suspend fun getCurrentUser(): User?

    suspend fun updateUserDisplayName(displayName: String)
}
