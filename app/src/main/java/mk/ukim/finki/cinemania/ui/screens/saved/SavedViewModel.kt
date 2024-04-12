package mk.ukim.finki.cinemania.ui.screens.saved

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
import mk.ukim.finki.cinemania.ui.screens.adapters.MovieItem

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<SavedState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<SavedState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true

            val watchlistMovieIds = authRepository.getCurrentUser()?.uid?.let {
                userRepository.getWatchlist(
                    it
                )
            }

            val favoritesMovieIds = authRepository.getCurrentUser()?.uid?.let {
                userRepository.getFavorites(
                    it
                )
            }

            val watchedMovieIds = authRepository.getCurrentUser()?.uid?.let {
                userRepository.getWatchedMovies(
                    it
                )
            }

            val watchlistMovieList = watchlistMovieIds?.map { movieId ->
                movieRepository.fetchMovieById(movieId)
            }

            val favoritesMovieList = favoritesMovieIds?.map { movieId ->
                movieRepository.fetchMovieById(movieId)
            }

            val watchedMovieList = watchedMovieIds?.map { movieId ->
                movieRepository.fetchMovieById(movieId)
            }

            _stateFlow.value = SavedState(watchlistMovieList?.map { MovieItem(it) } ?: emptyList(),
                favoritesMovieList?.map { MovieItem(it) } ?: emptyList(),
                watchedMovieList?.map { MovieItem(it) } ?: emptyList())
            _loadingStateFlow.value = false
        }
    }
}
