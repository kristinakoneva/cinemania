package mk.ukim.finki.cinemania.ui.screens.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.domain.movie.MovieRepository

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<MovieDetailsState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<MovieDetailsState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    fun initMovieDetails(movieId: Int) {
        if (_stateFlow.value != null) return

        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            val movieDetails = movieRepository.fetchMovieDetailsById(movieId)
            _stateFlow.value = MovieDetailsState(movieDetails)
            _loadingStateFlow.value = false
        }
    }
}
