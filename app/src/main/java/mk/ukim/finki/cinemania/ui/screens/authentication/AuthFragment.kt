package mk.ukim.finki.cinemania.ui.screens.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.FragmentAuthBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.screens.MainActivity
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog
import mk.ukim.finki.cinemania.utils.Constants.LOADING_DIALOG_TAG

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

    private val viewModel by viewModels<AuthViewModel>()

    private var loadingDialog: LoadingDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoadingState()
        observeAuthenticationState()
        initViews()
    }

    private fun initViews() = with(binding) {
        actionButton.setOnClickListener { validateInputs() }
        secondaryActionButton.setOnClickListener { viewModel.switchView() }
        editTextEmail.addTextChangedListener {
            resetErrors()
        }
        editTextPassword.addTextChangedListener {
            resetErrors()
        }
        editTextName.addTextChangedListener {
            resetErrors()
        }
    }

    private fun observeAuthenticationState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    state?.let {
                        when (state) {
                            is AuthState.AlreadyLoggedIn -> navigateToMain()

                            is AuthState.Login -> initLoginView()

                            is AuthState.Register -> initRegisterView()

                            is AuthState.Success -> {
                                Toast.makeText(requireContext(), "Authentication successful!", Toast.LENGTH_SHORT).show()
                                navigateToMain()
                            }

                            is AuthState.Error -> {
                                Toast.makeText(requireContext(), "Authentication unsuccessful!", Toast.LENGTH_SHORT).show()
                                resetInputFields()
                                viewModel.resetState()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initLoginView() = with(binding) {
        resetInputFields()
        title.text = getString(R.string.login)
        actionButton.text = getString(R.string.login)
        descriptionText.text = getString(R.string.description_no_account)
        secondaryActionButton.text = getString(R.string.register)
        groupRegister.visibility = View.GONE
    }

    private fun initRegisterView() = with(binding) {
        resetInputFields()
        title.text = getString(R.string.register)
        actionButton.text = getString(R.string.register)
        descriptionText.text = getString(R.string.description_already_have_account)
        secondaryActionButton.text = getString(R.string.login)
        groupRegister.visibility = View.VISIBLE
    }

    private fun resetInputFields() = with(binding) {
        resetErrors()
        editTextEmail.text?.clear()
        editTextPassword.text?.clear()
        editTextName.text?.clear()
        editTextEmail.clearFocus()
        editTextPassword.clearFocus()
        editTextName.clearFocus()
    }

    private fun resetErrors() = with(binding) {
        textInputLayoutEmail.error = null
        textInputLayoutPassword.error = null
        textInputLayoutName.error = null
    }

    private fun validateInputs() = with(binding) {
        resetErrors()

        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (email.isEmpty()) {
            textInputLayoutEmail.error = "Email is required"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutEmail.error = "Enter a valid email address"
            return
        }

        if (password.isEmpty()) {
            textInputLayoutPassword.error = "Password is required"
            return
        }

        if (password.length < 6) {
            textInputLayoutPassword.error = "Password must be at least 6 characters"
            return
        }

        if (!viewModel.isLogin) {
            val name = editTextName.text.toString().trim()

            if (name.isEmpty()) {
                textInputLayoutName.error = "Name is required"
                return
            }
        }

        viewModel.onActionButtonClicked(email, password, editTextName.text.toString().trim())
    }

    private fun navigateToMain() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingStateFlow.collect { isLoading ->
                    if (isLoading) {
                        loadingDialog = LoadingDialog()
                        loadingDialog?.show(childFragmentManager, LOADING_DIALOG_TAG)
                    } else {
                        loadingDialog?.dismiss()
                    }
                }
            }
        }
    }
}
