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
import mk.ukim.finki.cinemania.domain.firestore.FirestoreRepository
import mk.ukim.finki.cinemania.domain.movie.MovieRepository

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<SavedState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<SavedState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            // TODO: Implement logic for fetching actual saved user choices. Displaying the popular movies for now.
            val movieList = movieRepository.fetchPopularMovieList()

            val firestoreWatchlistMovieIds = authRepository.getCurrentUser()?.uid?.let {
                firestoreRepository.getWatchlist(
                    it
                )
            }

            val firestoreFavoritesMovieIds = authRepository.getCurrentUser()?.uid?.let {
                firestoreRepository.getFavorites(
                    it
                )
            }

            val firestoreWatchedMovieIds = authRepository.getCurrentUser()?.uid?.let {
                firestoreRepository.getWatchedMovies(
                    it
                )
            }

            val firestoreWatchlistMovieList = firestoreWatchlistMovieIds?.map { movieId ->
                movieRepository.fetchMovieById(movieId)
            }

            val firestoreFavoritesMovieList = firestoreFavoritesMovieIds?.map { movieId ->
                movieRepository.fetchMovieById(movieId)
            }

            val firestoreWatchedMovieList = firestoreWatchedMovieIds?.map { movieId ->
                movieRepository.fetchMovieById(movieId)
            }

            _stateFlow.value = SavedState(firestoreWatchlistMovieList ?: emptyList(),
                firestoreFavoritesMovieList ?: emptyList(),
                firestoreWatchedMovieList ?: emptyList())
            _loadingStateFlow.value = false
        }
    }
}
