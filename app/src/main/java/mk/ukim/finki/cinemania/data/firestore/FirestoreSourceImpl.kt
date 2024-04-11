package mk.ukim.finki.cinemania.data.firestore

import android.util.Log
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
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

    override suspend fun addToFavorites(movieId: Int, userId: String) {
        db.collection("users").document(userId)
            .update("favorites", FieldValue.arrayUnion(movieId))
            .await()
            .also {
                Log.d("Firestore", "Added movie $movieId to favorites for user $userId")
            }
    }

    override suspend fun addToWatched(movieId: Int, userId: String) {
        db.collection("users").document(userId)
            .update("watched", FieldValue.arrayUnion(movieId))
            .await()
            .also {
                Log.d("Firestore", "Added movie $movieId to favorites for user $userId")
            }
    }

    override suspend fun addToWatchlist(movieId: Int, userId: String) {
        db.collection("users").document(userId)
            .update("watchlist", FieldValue.arrayUnion(movieId))
            .await()
            .also {
                Log.d("Firestore", "Added movie $movieId to favorites for user $userId")
            }
    }

    override suspend fun createUserDocument(userId: String) {
        try {
            val userRef = db.collection("users").document(userId)
            val document = userRef.get().await()

            if (!document.exists()) {
                val emptyUserData = hashMapOf<String, Any>()
                await(userRef.set(emptyUserData))
            } else {
                Log.d("Firestore", "User document already exists for: $userId")
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirestoreError", "Error creating user document: ${e.message}", e)
        }
    }

    override suspend fun getUserDocument(userId: String): DocumentSnapshot? {
        try {
            val userRef = db.collection("users").document(userId)
            return userRef.get().await()
        } catch (e: FirebaseFirestoreException) {
            Log.e("FirestoreError", "Error fetching user document: ${e.message}", e)
            return null
        }
    }

}