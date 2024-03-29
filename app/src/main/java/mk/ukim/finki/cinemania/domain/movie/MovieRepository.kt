package mk.ukim.finki.cinemania.domain.movie

import mk.ukim.finki.cinemania.domain.models.Movie

interface MovieRepository {

    suspend fun fetchPopularMovieList(): List<Movie>

    suspend fun searchMovies(query: String): List<Movie>
}
