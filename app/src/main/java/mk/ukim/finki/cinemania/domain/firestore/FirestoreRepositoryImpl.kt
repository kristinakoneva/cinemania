package mk.ukim.finki.cinemania.domain.firestore

import com.google.firebase.firestore.DocumentSnapshot
import mk.ukim.finki.cinemania.data.firestore.FirestoreSource
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestoreSource: FirestoreSource
) : FirestoreRepository {
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

    override suspend fun addToWatched(movieId: Int, userId: String) {
        firestoreSource.addToWatched(movieId, userId)
    }

    override suspend fun addToWatchlist(movieId: Int, userId: String) {
        firestoreSource.addToWatchlist(movieId, userId)
    }

    override suspend fun createUserDocument(userId: String) {
        firestoreSource.createUserDocument(userId)
    }

    override suspend fun getUserDocument(userId: String): DocumentSnapshot? {
        return firestoreSource.getUserDocument(userId)
    }

}