package mk.ukim.finki.cinemania.ui.screens.authentication

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

@HiltViewModel
class AuthViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository,
                                        private val firestoreRepository: UserRepository) : ViewModel() {

    private val _stateFlow: MutableStateFlow<AuthState?> = MutableStateFlow(null)
    val stateFlow: StateFlow<AuthState?> = _stateFlow

    private val _loadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingStateFlow: StateFlow<Boolean> = _loadingStateFlow

    var isLogin: Boolean = true

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            try {
                if (authenticationRepository.getCurrentUser() != null) {
                    _stateFlow.value = AuthState.AlreadyLoggedIn()
                } else {
                    _stateFlow.value = AuthState.Login()
                }
            } catch (e: Exception) {
                _stateFlow.value = AuthState.Login()
            } finally {
                _loadingStateFlow.value = false
            }
        }
    }

    fun onActionButtonClicked(email: String, password: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStateFlow.value = true
            try {
                if (isLogin) {
                    authenticationRepository.loginUser(email, password)
                } else {
                    authenticationRepository.registerUser(email, password)
                }

                val user = authenticationRepository.getCurrentUser()
                if (user != null) {
                    firestoreRepository.createUserDocument(user.uid)

                    if (!isLogin) {
                        authenticationRepository.updateUserDisplayName(name)
                    }
                    _stateFlow.value = AuthState.Success()
                } else {
                    _stateFlow.value = AuthState.Error()
                }
            } catch (e: Exception) {
                _stateFlow.value = AuthState.Error()
            } finally {
                _loadingStateFlow.value = false
            }
        }
    }

    fun switchView() {
        isLogin = !isLogin
        resetState()
    }

    fun resetState() {
        if (isLogin) {
            _stateFlow.value = AuthState.Login()
        } else {
            _stateFlow.value = AuthState.Register()
        }
    }
}
