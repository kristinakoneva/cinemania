package mk.ukim.finki.cinemania.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.domain.authentication.AuthenticationRepository
import mk.ukim.finki.cinemania.domain.models.User
import mk.ukim.finki.cinemania.domain.movie.MovieRepository
import mk.ukim.finki.cinemania.domain.user.UserRepository
import mk.ukim.finki.cinemania.ui.screens.adapters.MovieItem

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<ProfileState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<ProfileState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    private var watchlistIds = emptyList<String>()
    private var favoritesIds = emptyList<String>()
    private var watchedIds = emptyList<String>()

    private var user: User? = null

    private var likedMovieRecommendationsName: String? = null
    private var watchedMovieRecommendations = emptyList<MovieItem>()
    private var watchedMovieRecommendationsName: String? = null
    private var likedMovieRecommendations = emptyList<MovieItem>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            val name = authenticationRepository.getCurrentUser()?.displayName.orEmpty()
            user = authenticationRepository.getCurrentUser()
            setRecommendations()
            val topRatedMovies = movieRepository.fetchTopRatedMovies().map { MovieItem(movie = it) }

            _stateFlow.value = ProfileState(
                name = name,
                watchedMovieRecommendationsName = watchedMovieRecommendationsName,
                watchedMovieRecommendations = watchedMovieRecommendations,
                likedMovieRecommendationsName = likedMovieRecommendationsName,
                likedMovieRecommendations = likedMovieRecommendations,
                topRatedMovies = topRatedMovies
            )
            _loadingStateFlow.value = false
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.logoutUser()
        }
    }

    fun editName(newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            authenticationRepository.updateUserDisplayName(newName)
            _stateFlow.value = _stateFlow.value?.copy(name = newName)
            _loadingStateFlow.value = false
        }
    }

    private suspend fun setRecommendations() {
        likedMovieRecommendationsName = null
        watchedMovieRecommendationsName = null

        val userId = user?.uid
        if (userId != null) {
            favoritesIds = userRepository.getFavorites(userId).map { it.toString() }
            watchedIds = userRepository.getWatchedMovies(userId).map { it.toString() }
            watchlistIds = userRepository.getWatchlist(userId).map { it.toString() }
        }

        if (favoritesIds.isNotEmpty()) {
            val randomLikedMovieId = favoritesIds.random()
            likedMovieRecommendations =
                movieRepository.fetchMovieRecommendationsForMovieId(randomLikedMovieId.toInt()).map { MovieItem(movie = it) }
            likedMovieRecommendationsName = movieRepository.fetchMovieById(randomLikedMovieId.toInt()).title
        }
        if (watchedIds.isNotEmpty()) {
            val randomWatchedMovieId = watchedIds.random()
            watchedMovieRecommendations =
                movieRepository.fetchMovieRecommendationsForMovieId(randomWatchedMovieId.toInt()).map { MovieItem(movie = it) }
            watchedMovieRecommendationsName = movieRepository.fetchMovieById(randomWatchedMovieId.toInt()).title
        }
    }

    fun refreshRecommendations() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            setRecommendations()
            _stateFlow.value = _stateFlow.value?.copy(
                watchedMovieRecommendationsName = watchedMovieRecommendationsName,
                watchedMovieRecommendations = watchedMovieRecommendations,
                likedMovieRecommendationsName = likedMovieRecommendationsName,
                likedMovieRecommendations = likedMovieRecommendations,
            )
            _loadingStateFlow.value = false
        }
    }
}
