package mk.ukim.finki.cinemania.ui.screens.explore

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.FragmentExploreBinding
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.screens.adapters.MovieAdapter
import mk.ukim.finki.cinemania.utils.Constants.LOADING_DIALOG_TAG
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private val binding by viewBinding(FragmentExploreBinding::bind)

    private val viewModel by viewModels<ExploreViewModel>()

    private var loadingDialog: LoadingDialog? = null

    private lateinit var adapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        super.onViewCreated(view, savedInstanceState)
        initSearchView()
        initListeners()
        collectStateFlow()
    }

    private fun initAdapter() = with(binding) {
        adapter = MovieAdapter(items = emptyList()) { movie ->
            Toast.makeText(requireContext(), "Clicked on " + movie.title, Toast.LENGTH_SHORT).show()
        }
        moviesRecyclerView.adapter = adapter
    }

    private fun initSearchView() = with(binding) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchMovies(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.showPopularMovies()
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.showPopularMovies()
            true
        }
    }

    private fun initListeners() {
        binding.showPopularMoviesButton.setOnClickListener {
            binding.searchView.setQuery("", false)
            viewModel.showPopularMovies()
        }
    }

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    state?.let {
                        with(binding) {
                            if (state.movieList.isEmpty()) {
                                noMoviesFoundGroup.visibility = View.VISIBLE
                                moviesRecyclerView.visibility = View.GONE
                            } else {
                                noMoviesFoundGroup.visibility = View.GONE
                                moviesRecyclerView.visibility = View.VISIBLE
                                adapter.submitList(state.movieList)
                            }
                        }

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
