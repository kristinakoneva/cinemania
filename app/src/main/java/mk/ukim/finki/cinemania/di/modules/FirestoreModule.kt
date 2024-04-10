package mk.ukim.finki.cinemania.di.modules

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirestoreModule {
    @Provides
    fun providesFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}