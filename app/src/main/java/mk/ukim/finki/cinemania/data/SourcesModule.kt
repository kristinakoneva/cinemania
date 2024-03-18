package mk.ukim.finki.cinemania.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mk.ukim.finki.cinemania.data.api.MoviesApiSource
import mk.ukim.finki.cinemania.data.api.MoviesApiSourceImpl

@Module
@InstallIn(SingletonComponent::class)
interface SourcesModule {

    @Binds
    fun bindMoviesApiSource(moviesApiSourceImpl: MoviesApiSourceImpl): MoviesApiSource
}