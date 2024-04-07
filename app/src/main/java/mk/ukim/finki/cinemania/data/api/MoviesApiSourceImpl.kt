package mk.ukim.finki.cinemania.data.api

import javax.inject.Inject
import mk.ukim.finki.cinemania.data.api.models.MovieDetailsResource
import mk.ukim.finki.cinemania.data.api.models.MovieListResponse

class MoviesApiSourceImpl @Inject constructor(
    private val apiService: MoviesApiService
) : MoviesApiSource {

    override suspend fun fetchPopularMovieList(): MovieListResponse = apiService.fetchPopularMovieList()

    override suspend fun searchMovies(query: String): MovieListResponse = apiService.searchMovies(query)

    override suspend fun fetchMovieDetailsById(movieId: Int): MovieDetailsResource = apiService.fetchMovieDetailsById(movieId)
}
