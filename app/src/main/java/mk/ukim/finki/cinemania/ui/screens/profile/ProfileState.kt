package mk.ukim.finki.cinemania.ui.screens.profile

import mk.ukim.finki.cinemania.domain.models.Movie

data class ProfileState(
    val name: String,
    val watchedMovieRecommendationsName: String,
    val watchedMovieRecommendations: List<Movie>,
    val likedMovieRecommendationsName: String,
    val likedMovieRecommendations: List<Movie>,
)
