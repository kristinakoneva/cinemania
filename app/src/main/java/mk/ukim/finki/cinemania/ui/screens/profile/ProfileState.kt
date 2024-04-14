package mk.ukim.finki.cinemania.ui.screens.profile

import mk.ukim.finki.cinemania.ui.screens.adapters.MovieItem

data class ProfileState(
    val name: String,
    val watchedMovieRecommendationsName: String,
    val watchedMovieRecommendations: List<MovieItem>,
    val likedMovieRecommendationsName: String,
    val likedMovieRecommendations: List<MovieItem>,
)
