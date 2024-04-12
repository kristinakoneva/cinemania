package mk.ukim.finki.cinemania.ui.screens.saved

import mk.ukim.finki.cinemania.ui.screens.adapters.MovieItem

data class SavedState(
    val watchLaterMovies: List<MovieItem>,
    val favoriteMovies: List<MovieItem>,
    val watchedMovies: List<MovieItem>
)
