package mk.ukim.finki.cinemania.ui.screens.saved

import android.os.Bundle
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
import mk.ukim.finki.cinemania.databinding.FragmentSavedBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.screens.adapters.MovieAdapter
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog
import mk.ukim.finki.cinemania.utils.Constants

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved) {

    private val binding by viewBinding(FragmentSavedBinding::bind)

    private val viewModel by viewModels<SavedViewModel>()

    private var loadingDialog: LoadingDialog? = null

    private lateinit var watchLaterAdapter: MovieAdapter

    private lateinit var favoritesAdapter: MovieAdapter

    private lateinit var watchedAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapters()
        super.onViewCreated(view, savedInstanceState)
        collectStateFlow()
    }

    private fun initAdapters() = with(binding) {
        watchLaterAdapter = MovieAdapter(items = emptyList(), areActionsVisible = false) { movie ->
            Toast.makeText(requireContext(), "Clicked on " + movie.title, Toast.LENGTH_SHORT).show()
        }
        watchLaterMoviesRecyclerView.adapter = watchLaterAdapter

        favoritesAdapter = MovieAdapter(items = emptyList(), areActionsVisible = false) { movie ->
            Toast.makeText(requireContext(), "Clicked on " + movie.title, Toast.LENGTH_SHORT).show()
        }
        favoriteMoviesRecyclerView.adapter = favoritesAdapter

        watchedAdapter = MovieAdapter(items = emptyList(), areActionsVisible = false) { movie ->
            Toast.makeText(requireContext(), "Clicked on " + movie.title, Toast.LENGTH_SHORT).show()
        }
        watchedMoviesRecyclerView.adapter = watchedAdapter
    }

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    state?.let {
                        with(binding) {
                            // TODO: Add empty states
                            watchLaterAdapter.submitList(state.watchLaterMovies)
                            favoritesAdapter.submitList(state.favoriteMovies)
                            watchedAdapter.submitList(state.watchedMovies)
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
}

