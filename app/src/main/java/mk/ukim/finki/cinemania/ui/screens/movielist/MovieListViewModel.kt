package mk.ukim.finki.cinemania.ui.screens.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MovieListViewModel @Inject constructor() : ViewModel() {

    private val _stateFlow: MutableStateFlow<MovieListState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<MovieListState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            // delaying for 2 seconds to simulate a network call
            delay(2000L)
            _stateFlow.value = MovieListState("Movie List", emptyList())
            _loadingStateFlow.value = false
        }
    }
}