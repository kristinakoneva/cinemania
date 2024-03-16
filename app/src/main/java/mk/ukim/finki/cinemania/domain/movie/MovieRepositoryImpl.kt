package mk.ukim.finki.cinemania.domain.movie

import javax.inject.Inject
import mk.ukim.finki.cinemania.data.api.MoviesApiSource
import mk.ukim.finki.cinemania.domain.models.Movie

class MovieRepositoryImpl @Inject constructor(
    private val movieApiSource: MoviesApiSource
) : MovieRepository {
    override suspend fun fetchPopularMovieList(): List<Movie> {
        return movieApiSource.fetchPopularMovieList().results.map { movieResource ->
            Movie(
                title = movieResource.title,
                posterImage = movieResource.posterPath
            )
        }
    }
}