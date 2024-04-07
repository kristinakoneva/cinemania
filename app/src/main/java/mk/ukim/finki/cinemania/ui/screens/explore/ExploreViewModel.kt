package mk.ukim.finki.cinemania.ui.screens.explore

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
class ExploreViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<ExploreState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<ExploreState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    init {
        showPopularMovies()
    }

    fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            val movieList = movieRepository.searchMovies(query)
            _stateFlow.value = ExploreState(movieList)
            _loadingStateFlow.value = false
        }
    }

    fun showPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            val movieList = movieRepository.fetchPopularMovieList()
            _stateFlow.value = ExploreState(movieList)
            _loadingStateFlow.value = false
        }
    }
}
