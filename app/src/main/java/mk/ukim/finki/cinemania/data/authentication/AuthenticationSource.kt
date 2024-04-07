package mk.ukim.finki.cinemania.data.authentication

import com.google.firebase.auth.FirebaseUser

interface AuthenticationSource {

    suspend fun registerUser(email: String, password: String): Boolean

    suspend fun loginUser(email: String, password: String): Boolean

    suspend fun getCurrentUser(): FirebaseUser?

    suspend fun updateUserDisplayName(displayName: String)

    suspend fun logoutUser()
}
