package mk.ukim.finki.cinemania.data.firestore

import android.util.Log
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class FirestoreSourceImpl @Inject constructor(private val db: FirebaseFirestore) : FirestoreSource {
    override suspend fun getFavorites(userId: String): List<Int> {
        return try {
            db.collection("users")
                .document(userId)
                .get()
                .await()
                .get("favorites") as? List<Int> ?: emptyList()
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirestoreError", "Error fetching favorites: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getWatchedMovies(userId: String): List<Int> {
        return try {
            db.collection("users")
                .document(userId)
                .get()
                .await()
                .get("watched") as? List<Int> ?: emptyList()
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirestoreError", "Error fetching watched: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getWatchlist(userId: String): List<Int> {
        return try {
            db.collection("users")
                .document(userId)
                .get()
                .await()
                .get("watchlist") as? List<Int> ?: emptyList()
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirestoreError", "Error fetching watchlist: ${e.message}", e)
            emptyList()
        }
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