package mk.ukim.finki.cinemania.domain.firestore

import com.google.firebase.firestore.DocumentSnapshot

interface FirestoreRepository {
    suspend fun getFavorites(userId: String): List<Int>

    suspend fun getWatchedMovies(userId: String): List<Int>

    suspend fun getWatchlist(userId: String): List<Int>

    suspend fun addToFavorites(movieId: Int, userId: String)

    suspend fun addToWatched(movieId: Int, userId: String)

    suspend fun addToWatchlist(movieId: Int, userId: String)

    suspend fun createUserDocument(userId: String)

    suspend fun getUserDocument(userId: String): DocumentSnapshot?
}