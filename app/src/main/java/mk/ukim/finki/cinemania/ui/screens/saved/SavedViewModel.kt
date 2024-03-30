package mk.ukim.finki.cinemania.ui.screens.saved

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
class SavedViewModel @Inject constructor(
    private val movieRepository: MovieRepository
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
            _stateFlow.value = SavedState(movieList, movieList, movieList)
            _loadingStateFlow.value = false
        }
    }
}
