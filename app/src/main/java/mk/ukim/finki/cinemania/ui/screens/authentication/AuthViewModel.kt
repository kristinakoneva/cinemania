package mk.ukim.finki.cinemania.ui.screens.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    sealed class AuthenticationState {
        object Loading : AuthenticationState()
        class Authenticated(val user: FirebaseUser) : AuthenticationState()
        class Error(val error: String) : AuthenticationState()
    }

    private val _authenticationState = MutableStateFlow<AuthenticationState>(AuthenticationState.Loading)
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState

    fun signIn(email: String, password: String) {
        _authenticationState.value = AuthenticationState.Loading
        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                _authenticationState.value = AuthenticationState.Authenticated(authResult.user!!)
            } catch (e: Exception) {
                _authenticationState.value = AuthenticationState.Error(e.message ?: "Authentication failed")
            }
        }
    }

//    init {
//        _authenticationState.value = AuthenticationState.Loading
//        viewModelScope.launch {
//            try {
//                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
//                _authenticationState.value = AuthenticationState.Authenticated(authResult.user!!)
//            } catch (e: Exception) {
//                _authenticationState.value = AuthenticationState.Error(e.message ?: "Authentication failed")
//            }
//        }
//    }
}
