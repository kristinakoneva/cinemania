package mk.ukim.finki.cinemania.data.api

import mk.ukim.finki.cinemania.data.api.models.MovieDetailsResource
import mk.ukim.finki.cinemania.data.api.models.MovieListResponse

interface MoviesApiSource {

    suspend fun fetchPopularMovieList(): MovieListResponse

    suspend fun searchMovies(query: String): MovieListResponse

    suspend fun fetchMovieDetailsById(movieId: Int): MovieDetailsResource

    suspend fun fetchMovieRecommendationsForMovieId(movieId: Int): MovieListResponse
}
