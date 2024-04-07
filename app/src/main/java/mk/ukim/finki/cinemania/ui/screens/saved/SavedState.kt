package mk.ukim.finki.cinemania.ui.screens.saved

import mk.ukim.finki.cinemania.domain.models.Movie

data class SavedState(
    val watchLaterMovies: List<Movie>,
    val favoriteMovies: List<Movie>,
    val watchedMovies: List<Movie>
)
