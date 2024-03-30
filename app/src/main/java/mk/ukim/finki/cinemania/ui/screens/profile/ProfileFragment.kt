package mk.ukim.finki.cinemania.ui.screens.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.DialogEditNameBinding
import mk.ukim.finki.cinemania.databinding.FragmentProfileBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.screens.adapters.MovieAdapter
import mk.ukim.finki.cinemania.ui.screens.authentication.AuthActivity
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog
import mk.ukim.finki.cinemania.utils.Constants

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel by viewModels<ProfileViewModel>()

    private var loadingDialog: LoadingDialog? = null

    private lateinit var watchedMovieRecommendationsAdapter: MovieAdapter

    private lateinit var likedMovieRecommendationsAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapters()
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        collectStateFlow()
    }

    private fun initAdapters() = with(binding) {
        watchedMovieRecommendationsAdapter = MovieAdapter(items = emptyList(), areActionsVisible = false) { movie ->
            Toast.makeText(requireContext(), "Clicked on " + movie.title, Toast.LENGTH_SHORT).show()
        }
        watchedMovieRecommendationsRecyclerView.adapter = watchedMovieRecommendationsAdapter

        likedMovieRecommendationsAdapter = MovieAdapter(items = emptyList(), areActionsVisible = false) { movie ->
            Toast.makeText(requireContext(), "Clicked on " + movie.title, Toast.LENGTH_SHORT).show()
        }
        likedMovieRecommendationsRecyclerView.adapter = likedMovieRecommendationsAdapter
    }

    private fun initListeners() = with(binding) {
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        editNameButton.setOnClickListener {
            showEditNameDialog()
        }
    }

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    state?.let {
                        with(binding) {
                            name.text = state.name
                            if (state.watchedMovieRecommendations.isNotEmpty()) {
                                watchedMovieRecommendationsAdapter.submitList(state.watchedMovieRecommendations)
                                watchedMovieRecommendationsRecyclerView.visibility = View.VISIBLE
                                becauseYouWatchedTitle.visibility = View.VISIBLE
                                becauseYouWatchedTitle.text = getString(R.string.because_you_watched, state.watchedMovieRecommendationsName)
                            } else {
                                watchedMovieRecommendationsRecyclerView.visibility = View.GONE
                                becauseYouWatchedTitle.visibility = View.GONE
                            }
                            if (state.likedMovieRecommendations.isNotEmpty()) {
                                likedMovieRecommendationsAdapter.submitList(state.likedMovieRecommendations)
                                likedMovieRecommendationsRecyclerView.visibility = View.VISIBLE
                                becauseYouLikedTitle.visibility = View.VISIBLE
                                becauseYouLikedTitle.text = getString(R.string.because_you_liked, state.likedMovieRecommendationsName)
                            } else {
                                likedMovieRecommendationsRecyclerView.visibility = View.GONE
                                becauseYouLikedTitle.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingStateFlow.collect { isLoading ->
                    if (isLoading && loadingDialog == null && childFragmentManager.findFragmentByTag(Constants.LOADING_DIALOG_TAG) == null) {
                        loadingDialog = LoadingDialog()
                        loadingDialog?.show(childFragmentManager, Constants.LOADING_DIALOG_TAG)
                    } else {
                        loadingDialog?.dismiss()
                        loadingDialog = null
                    }
                }
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout)
            .setMessage(R.string.description_logout_confirmation)
            .setPositiveButton(R.string.logout) { _, _ ->
                viewModel.logout()
                startActivity(Intent(requireContext(), AuthActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun showEditNameDialog() {
        val dialogView = DialogEditNameBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.edit_name)
            .setView(dialogView.root)
            .setPositiveButton(R.string.save) { dialog, _ ->
                if (dialogView.editTextNewName.text.toString().isNotEmpty() && dialogView.editTextNewName.text != null) {
                    viewModel.editName(dialogView.editTextNewName.text?.trim().toString())
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}
