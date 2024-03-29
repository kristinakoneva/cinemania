package mk.ukim.finki.cinemania.domain.authentication

import javax.inject.Inject
import mk.ukim.finki.cinemania.data.authentication.AuthenticationSource
import mk.ukim.finki.cinemania.domain.models.User

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationSource: AuthenticationSource
) : AuthenticationRepository {
    override suspend fun registerUser(email: String, password: String): Boolean =
        authenticationSource.registerUser(email, password)

    override suspend fun loginUser(email: String, password: String): Boolean =
        authenticationSource.loginUser(email, password)

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = authenticationSource.getCurrentUser() ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email.orEmpty(),
            displayName = firebaseUser.displayName.orEmpty(),
            photoUrl = firebaseUser.photoUrl.toString()
        )
    }

    override suspend fun updateUserDisplayName(displayName: String) =
        authenticationSource.updateUserDisplayName(displayName)
}
