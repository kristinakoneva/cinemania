package mk.ukim.finki.cinemania.ui.screens.adapters

import mk.ukim.finki.cinemania.domain.models.Movie

data class MovieItem(
    val movie: Movie,
    val isAddedToWatchLater: Boolean? = null,
    val isAddedToFavorites: Boolean? = null,
    val isAddedToWatched: Boolean? = null
)
