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

    private var watchlistIds = emptyList<String>()
    private var favoritesIds = emptyList<String>()
    private var watchedIds = emptyList<String>()

    private var userId: String = ""

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
        if (user != null) {
            userId = user.uid
            favoritesIds = userRepository.getFavorites(userId).map { it.toString() }
            watchedIds = userRepository.getWatchedMovies(userId).map { it.toString() }
            watchlistIds = userRepository.getWatchlist(userId).map { it.toString() }
        }

        refreshItemActionsState()
    }

    fun onFavoriteActionSelected(movieId: Int, isSelected: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesIds = if (isSelected) {
                userRepository.removeFromFavorites(movieId, userId)
                favoritesIds.filter { it != movieId.toString() }
            } else {
                userRepository.addToFavorites(movieId, userId)
                favoritesIds + listOf(movieId.toString())
            }
            refreshItemActionsState()
        }
    }

    fun onWatchLaterActionSelected(movieId: Int, isSelected: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            watchlistIds = if (isSelected) {
                userRepository.removeFromWatchlist(movieId, userId)
                watchlistIds.filter { it != movieId.toString() }
            } else {
                userRepository.addToWatchlist(movieId, userId)
                watchlistIds + listOf(movieId.toString())
            }
            refreshItemActionsState()
        }
    }

    fun onWatchedActionSelected(movieId: Int, isSelected: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            watchedIds = if (isSelected) {
                userRepository.removeFromWatched(movieId, userId)
                watchedIds.filter { it != movieId.toString() }
            } else {
                userRepository.addToWatched(movieId, userId)
                watchedIds + listOf(movieId.toString())
            }
            refreshItemActionsState()
        }
    }

    private fun refreshItemActionsState() {
        val movieItems = movieList.map {
            MovieItem(
                movie = it,
                isAddedToFavorites = favoritesIds.contains(it.id.toString()),
                isAddedToWatched = watchedIds.contains(it.id.toString()),
                isAddedToWatchLater = watchlistIds.contains(it.id.toString())
            )
        }
        _stateFlow.value = ExploreState(movieItems)
    }
}
