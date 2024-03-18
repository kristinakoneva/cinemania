package mk.ukim.finki.cinemania.data.api

import mk.ukim.finki.cinemania.data.api.models.MovieListResponse

interface MoviesApiSource {

    suspend fun fetchPopularMovieList(): MovieListResponse
}