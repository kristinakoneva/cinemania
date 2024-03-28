package mk.ukim.finki.cinemania.ui.screens.authentication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.FragmentAuthBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog
import mk.ukim.finki.cinemania.utils.Constants.LOADING_DIALOG_TAG

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

    private val viewModel by viewModels<AuthViewModel>()

    private var loadingDialog: LoadingDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeAuthenticationState()
    }

    private fun initViews() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        viewModel.signIn(email, password)
    }

    private fun observeAuthenticationState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authenticationState.collect { authenticationState ->
                    when (authenticationState) {
                        is AuthViewModel.AuthenticationState.Authenticated -> {
                            Toast.makeText(requireContext(), "Authentication successful!", Toast.LENGTH_SHORT).show()
                        }
                        is AuthViewModel.AuthenticationState.Loading -> {
                            showLoading()
                        }
                        is AuthViewModel.AuthenticationState.Error -> {
                            Toast.makeText(requireContext(), "Authentication failed: ${authenticationState.error}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(childFragmentManager, LOADING_DIALOG_TAG)
        }
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}
