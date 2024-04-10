package mk.ukim.finki.cinemania.domain.firestore

interface FirestoreRepository {
    suspend fun getFavorites(userId: String): List<Int>

    suspend fun getWatchedMovies(userId: String): List<Int>

    suspend fun getWatchlist(userId: String): List<Int>

    suspend fun addToFavorites(movieId: Int)

    suspend fun addToWatched(movieId: Int)

    suspend fun addToWatchlist(movieId: Int)
}