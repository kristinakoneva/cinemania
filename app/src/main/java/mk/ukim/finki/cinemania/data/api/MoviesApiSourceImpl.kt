package mk.ukim.finki.cinemania.data.api

import javax.inject.Inject
import mk.ukim.finki.cinemania.data.api.models.MovieListResponse

class MoviesApiSourceImpl @Inject constructor(
    private val apiService: MoviesApiService
) : MoviesApiSource {
    override suspend fun fetchPopularMovieList(): MovieListResponse = apiService.fetchPopularMovieList()
}