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
import mk.ukim.finki.cinemania.domain.movie.MovieRepository

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<ProfileState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<ProfileState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            val name = authenticationRepository.getCurrentUser()?.displayName.orEmpty()
            // TODO: Implement logic for fetching actual recommendations. Displaying the popular movies for now.
            val movieList = movieRepository.fetchPopularMovieList()
            _stateFlow.value = ProfileState(
                name = name,
                watchedMovieRecommendationsName = "Inception",
                watchedMovieRecommendations = movieList,
                likedMovieRecommendationsName = "Titanic",
                likedMovieRecommendations = movieList
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
}
