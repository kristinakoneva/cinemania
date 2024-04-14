package mk.ukim.finki.cinemania.domain.user

import com.google.firebase.firestore.DocumentSnapshot
import mk.ukim.finki.cinemania.data.firestore.FirestoreSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestoreSource: FirestoreSource
) : UserRepository {
    override suspend fun getFavorites(userId: String): List<Int> {
        return firestoreSource.getFavorites(userId)
    }

    override suspend fun getWatchedMovies(userId: String): List<Int> {
        return firestoreSource.getWatchedMovies(userId)
    }

    override suspend fun getWatchlist(userId: String): List<Int> {
        return firestoreSource.getWatchlist(userId)
    }

    override suspend fun addToFavorites(movieId: Int, userId: String) {
        firestoreSource.addToFavorites(movieId, userId)
    }

    override suspend fun removeFromFavorites(movieId: Int, userId: String) {
        firestoreSource.removeFromFavorites(movieId, userId)
    }

    override suspend fun addToWatched(movieId: Int, userId: String) {
        firestoreSource.addToWatched(movieId, userId)
    }

    override suspend fun removeFromWatched(movieId: Int, userId: String) {
        firestoreSource.removeFromWatched(movieId, userId)
    }

    override suspend fun addToWatchlist(movieId: Int, userId: String) {
        firestoreSource.addToWatchlist(movieId, userId)
    }

    override suspend fun removeFromWatchlist(movieId: Int, userId: String) {
        firestoreSource.removeFromWatchlist(movieId, userId)
    }

    override suspend fun createUserDocument(userId: String) {
        firestoreSource.createUserDocument(userId)
    }

    override suspend fun getUserDocument(userId: String): DocumentSnapshot? {
        return firestoreSource.getUserDocument(userId)
    }
}
