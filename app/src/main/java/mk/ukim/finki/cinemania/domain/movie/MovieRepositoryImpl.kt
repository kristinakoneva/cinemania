package mk.ukim.finki.cinemania.domain.movie

import javax.inject.Inject
import mk.ukim.finki.cinemania.data.api.MoviesApiSource
import mk.ukim.finki.cinemania.domain.models.Movie
import mk.ukim.finki.cinemania.domain.models.MovieDetails
import mk.ukim.finki.cinemania.utils.Utils

class MovieRepositoryImpl @Inject constructor(
    private val movieApiSource: MoviesApiSource
) : MovieRepository {
    override suspend fun fetchPopularMovieList(): List<Movie> {
        return movieApiSource.fetchPopularMovieList().results.map { movieResource ->
            Movie(
                id = movieResource.id,
                title = movieResource.title,
                posterImage = Utils.createImageUrl(movieResource.posterPath)
            )
        }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        return movieApiSource.searchMovies(query).results.map { movieResource ->
            Movie(
                id = movieResource.id,
                title = movieResource.title,
                posterImage = Utils.createImageUrl(movieResource.posterPath)
            )
        }
    }

    override suspend fun fetchMovieDetailsById(movieId: Int): MovieDetails {
        return movieApiSource.fetchMovieDetailsById(movieId).let { movieDetailsResponse ->
            MovieDetails(
                id = movieDetailsResponse.id,
                title = movieDetailsResponse.title,
                posterImage = Utils.createImageUrl(movieDetailsResponse.posterPath),
                overview = movieDetailsResponse.overview,
                releaseDate = movieDetailsResponse.releaseDate,
                rating = movieDetailsResponse.voteAverage,
                productionCountries = movieDetailsResponse.productionCountries.map { countryResource ->
                    countryResource.name
                },
                genres = movieDetailsResponse.genres.map { genreResource ->
                    genreResource.name
                },
                spokenLanguages = movieDetailsResponse.spokenLanguages.map { languageResource ->
                    languageResource.name
                },
                duration = String.format("%d min", movieDetailsResponse.runtime),
            )
        }
    }

    override suspend fun fetchMovieById(movieId: Int): Movie {
        return movieApiSource.fetchMovieDetailsById(movieId).let { movieDetailsResponse ->
            Movie(
                id = movieDetailsResponse.id,
                title = movieDetailsResponse.title,
                posterImage = Utils.createImageUrl(movieDetailsResponse.posterPath)
            )
        }
    }

    override suspend fun fetchMovieRecommendationsForMovieId(movieId: Int): List<Movie> {
        return movieApiSource.fetchMovieRecommendationsForMovieId(movieId).results.map { movieResource ->
            Movie(
                id = movieResource.id,
                title = movieResource.title,
                posterImage = Utils.createImageUrl(movieResource.posterPath)
            )
        }
    }

    override suspend fun fetchTopRatedMovies(): List<Movie> {
        return movieApiSource.fetchTopRatedMovies().results.map { movieResource ->
            Movie(
                id = movieResource.id,
                title = movieResource.title,
                posterImage = Utils.createImageUrl(movieResource.posterPath)
            )
        }
    }
}
