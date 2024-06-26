package mk.ukim.finki.cinemania.ui.screens.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.domain.authentication.AuthenticationRepository
import mk.ukim.finki.cinemania.domain.user.UserRepository
import mk.ukim.finki.cinemania.domain.movie.MovieRepository

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<MovieDetailsState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<MovieDetailsState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    private var watchlistIds = emptyList<String>()
    private var favoritesIds = emptyList<String>()
    private var watchedIds = emptyList<String>()

    fun initMovieDetails(movieId: Int) {
        if (_stateFlow.value != null) return

        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            val movieDetails = movieRepository.fetchMovieDetailsById(movieId)
            val user = authenticationRepository.getCurrentUser()
            if (user != null) {
                watchlistIds = userRepository.getWatchlist(user.uid).map { it.toString() }
                favoritesIds = userRepository.getFavorites(user.uid).map { it.toString() }
                watchedIds = userRepository.getWatchedMovies(user.uid).map { it.toString() }
            }

            _stateFlow.value = MovieDetailsState(
                movieDetails = movieDetails,
                isAddedToWatchLater = watchlistIds.contains(movieDetails.id.toString()),
                isAddedToFavorites = favoritesIds.contains(movieDetails.id.toString()),
                isAddedToWatched = watchedIds.contains(movieDetails.id.toString())
            )
            _loadingStateFlow.value = false
        }
    }

    fun addToFavorites(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = authenticationRepository.getCurrentUser()
            if (currentUser != null) {
                userRepository.addToFavorites(movieId, currentUser.uid)
                favoritesIds = favoritesIds + listOf(movieId.toString())
                refreshActionsState()
            }
        }
    }

    fun addToWatched(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = authenticationRepository.getCurrentUser()
            if (currentUser != null) {
                userRepository.addToWatched(movieId, currentUser.uid)
                watchedIds = watchedIds + listOf(movieId.toString())
                refreshActionsState()
            }
        }
    }

    fun addToWatchlist(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = authenticationRepository.getCurrentUser()
            if (currentUser != null) {
                userRepository.addToWatchlist(movieId, currentUser.uid)
                watchlistIds = watchlistIds + listOf(movieId.toString())
                refreshActionsState()
            }
        }
    }

    fun removeFromFavorites(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = authenticationRepository.getCurrentUser()
            if (currentUser != null) {
                userRepository.removeFromFavorites(movieId, currentUser.uid)
                favoritesIds = favoritesIds.filter { it != movieId.toString() }
                refreshActionsState()
            }
        }
    }

    fun removeFromWatched(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = authenticationRepository.getCurrentUser()
            if (currentUser != null) {
                userRepository.removeFromWatched(movieId, currentUser.uid)
                watchedIds = watchedIds.filter { it != movieId.toString() }
                refreshActionsState()
            }
        }
    }

    fun removeFromWatchlist(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = authenticationRepository.getCurrentUser()
            if (currentUser != null) {
                userRepository.removeFromWatchlist(movieId, currentUser.uid)
                watchlistIds = watchlistIds.filter { it != movieId.toString() }
                refreshActionsState()
            }
        }
    }

    private fun refreshActionsState() {
        val movieId = _stateFlow.value?.movieDetails?.id.toString()
        _stateFlow.value = _stateFlow.value?.copy(
            isAddedToWatched = watchedIds.contains(movieId),
            isAddedToFavorites = favoritesIds.contains(movieId),
            isAddedToWatchLater = watchlistIds.contains(movieId)
        )
    }
}
