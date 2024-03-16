package mk.ukim.finki.cinemania.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mk.ukim.finki.cinemania.domain.movie.MovieRepository
import mk.ukim.finki.cinemania.domain.movie.MovieRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository
}