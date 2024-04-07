package mk.ukim.finki.cinemania.ui.screens.authentication

sealed interface AuthState {
    class AlreadyLoggedIn : AuthState

    class Login : AuthState

    class Register : AuthState

    class Success : AuthState

    class Error : AuthState
}
