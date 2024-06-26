package mk.ukim.finki.cinemania.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mk.ukim.finki.cinemania.data.api.MoviesApiSource
import mk.ukim.finki.cinemania.data.api.MoviesApiSourceImpl
import mk.ukim.finki.cinemania.data.authentication.AuthenticationSource
import mk.ukim.finki.cinemania.data.authentication.AuthenticationSourceImpl
import mk.ukim.finki.cinemania.data.firestore.FirestoreSource
import mk.ukim.finki.cinemania.data.firestore.FirestoreSourceImpl

@Module
@InstallIn(SingletonComponent::class)
interface SourcesModule {

    @Binds
    fun bindMoviesApiSource(moviesApiSourceImpl: MoviesApiSourceImpl): MoviesApiSource

    @Binds
    fun bindAuthenticationSource(authenticationSourceImpl: AuthenticationSourceImpl): AuthenticationSource

    @Binds
    fun bindFirestoreSource(firestoreSourceImpl: FirestoreSourceImpl): FirestoreSource
}