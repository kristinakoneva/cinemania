package mk.ukim.finki.cinemania.ui.screens.movielist

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
import mk.ukim.finki.cinemania.databinding.FragmentMovieListBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.screens.adapters.MovieAdapter
import mk.ukim.finki.cinemania.utils.Constants.LOADING_DIALOG_TAG
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val binding by viewBinding(FragmentMovieListBinding::bind)

    private val viewModel by viewModels<MovieListViewModel>()

    private var loadingDialog: LoadingDialog? = null

    private lateinit var adapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        super.onViewCreated(view, savedInstanceState)
        collectStateFlow()
    }

    private fun initAdapter() = with(binding) {
        adapter = MovieAdapter(items = emptyList()) { movie ->
            Toast.makeText(requireContext(), "Clicked on " + movie.title, Toast.LENGTH_SHORT).show()
        }
        moviesRecyclerView.adapter = adapter
    }

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    state?.let {
                        adapter.submitList(state.movieList)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingStateFlow.collect { isLoading ->
                    if (isLoading && loadingDialog == null && childFragmentManager.findFragmentByTag(LOADING_DIALOG_TAG) == null) {
                        loadingDialog = LoadingDialog()
                        loadingDialog?.show(childFragmentManager, LOADING_DIALOG_TAG)
                    } else {
                        loadingDialog?.dismiss()
                        loadingDialog = null
                    }
                }
            }
        }
    }
}