package mk.ukim.finki.cinemania.domain.firestore

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

    override suspend fun addToFavorites(movieId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun addToWatched(movieId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun addToWatchlist(movieId: Int) {
        TODO("Not yet implemented")
    }

}