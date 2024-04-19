package mk.ukim.finki.cinemania.domain.movie

import mk.ukim.finki.cinemania.domain.models.Movie
import mk.ukim.finki.cinemania.domain.models.MovieDetails

interface MovieRepository {

    suspend fun fetchPopularMovieList(): List<Movie>

    suspend fun searchMovies(query: String): List<Movie>

    suspend fun fetchMovieDetailsById(movieId: Int): MovieDetails

    suspend fun fetchMovieById(movieId: Int): Movie

    suspend fun fetchMovieRecommendationsForMovieId(movieId: Int): List<Movie>
}
