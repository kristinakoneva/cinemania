package mk.ukim.finki.cinemania.data.api

import mk.ukim.finki.cinemania.data.api.models.MovieListResponse
import retrofit2.http.GET

interface MoviesApiService {
    @GET("movie/popular")
    suspend fun fetchPopularMovieList(): MovieListResponse
}