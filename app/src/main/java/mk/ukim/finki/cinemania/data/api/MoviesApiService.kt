package mk.ukim.finki.cinemania.data.api

import mk.ukim.finki.cinemania.data.api.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movie/popular")
    suspend fun fetchPopularMovieList(): MovieListResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieListResponse
}
