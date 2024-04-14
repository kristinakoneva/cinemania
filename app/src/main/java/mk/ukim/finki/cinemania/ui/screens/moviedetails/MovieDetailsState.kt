package mk.ukim.finki.cinemania.ui.screens.moviedetails

import mk.ukim.finki.cinemania.domain.models.MovieDetails

data class MovieDetailsState(
    val movieDetails: MovieDetails,
    val isAddedToWatchLater: Boolean = false,
    val isAddedToFavorites: Boolean = false,
    val isAddedToWatched : Boolean = false
)
