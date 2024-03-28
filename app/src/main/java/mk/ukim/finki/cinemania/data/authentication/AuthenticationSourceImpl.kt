package mk.ukim.finki.cinemania.data.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthenticationSourceImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthenticationSource {
    override suspend fun registerUser(email: String, password: String): Boolean = try {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun loginUser(email: String, password: String): Boolean = try {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
}