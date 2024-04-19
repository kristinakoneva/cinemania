package mk.ukim.finki.cinemania.data.api

import mk.ukim.finki.cinemania.data.api.models.MovieDetailsResource
import mk.ukim.finki.cinemania.data.api.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movie/popular")
    suspend fun fetchPopularMovieList(): MovieListResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetailsById(@Path("movie_id") movieId: Int): MovieDetailsResource

    @GET("movie/{movie_id}/recommendations")
    suspend fun fetchMovieRecommendationsForMovieId(@Path("movie_id") movieId: Int): MovieListResponse
}
