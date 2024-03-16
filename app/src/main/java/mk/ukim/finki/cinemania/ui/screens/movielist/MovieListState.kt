package mk.ukim.finki.cinemania.ui.screens.movielist

import mk.ukim.finki.cinemania.domain.models.Movie

data class MovieListState(
    val movieList: List<Movie>
)