package mk.ukim.finki.cinemania.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.domain.authentication.AuthenticationRepository
import mk.ukim.finki.cinemania.domain.models.Movie
import mk.ukim.finki.cinemania.domain.movie.MovieRepository
import mk.ukim.finki.cinemania.domain.user.UserRepository
import mk.ukim.finki.cinemania.ui.screens.adapters.MovieItem

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<ExploreState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<ExploreState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    private var movieList = emptyList<Movie>()

    init {
        showPopularMovies()
    }

    fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            movieList = movieRepository.searchMovies(query)
            setState()
            _loadingStateFlow.value = false
        }
    }

    fun showPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            movieList = movieRepository.fetchPopularMovieList()
            setState()
            _loadingStateFlow.value = false
        }
    }

    private suspend fun setState() {
        val user = authRepository.getCurrentUser()
        var favoriteMoviesIds = emptyList<String>()
        var watchedMoviesIds = emptyList<String>()
        var watchlistMoviesIds = emptyList<String>()
        if (user != null) {
            favoriteMoviesIds = userRepository.getFavorites(user.uid).map { it.toString() }
            watchedMoviesIds = userRepository.getWatchedMovies(user.uid).map { it.toString() }
            watchlistMoviesIds = userRepository.getWatchlist(user.uid).map { it.toString() }
        }

        val movieItems = movieList.map {
            MovieItem(
                movie = it,
                isAddedToFavorites = favoriteMoviesIds.contains(it.id.toString()),
                isAddedToWatched = watchedMoviesIds.contains(it.id.toString()),
                isAddedToWatchLater = watchlistMoviesIds.contains(it.id.toString())
            )
        }
        _stateFlow.value = ExploreState(movieItems)
    }
}
