package mk.ukim.finki.cinemania.ui.screens.authentication

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import mk.ukim.finki.cinemania.domain.authentication.AuthenticationRepository

@HiltViewModel
class AuthViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository) : ViewModel()
