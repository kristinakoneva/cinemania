package mk.ukim.finki.cinemania.ui.screens.moviedetails

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.ActivityMovieDetailsBinding
import mk.ukim.finki.cinemania.domain.models.MovieDetails
import mk.ukim.finki.cinemania.extensions.viewBinding
import mk.ukim.finki.cinemania.ui.screens.main.MainActivity
import mk.ukim.finki.cinemania.ui.shared.LoadingDialog
import mk.ukim.finki.cinemania.utils.Constants

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    companion object {
        const val MOVIE_ID = "movie_id"
    }

    private val binding by viewBinding(ActivityMovieDetailsBinding::inflate)

    private val viewModel by viewModels<MovieDetailsViewModel>()

    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val movieId = intent.getIntExtra(MOVIE_ID, -1)
        if (movieId == -1) {
            Toast.makeText(this, getString(R.string.invalid_movie), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            viewModel.initMovieDetails(movieId)
        }
        initListeners()
        collectStateFlow()
    }

    private fun collectStateFlow() {
        lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    state?.let {
                        initUi(
                            it.movieDetails,
                            it.isAddedToWatchLater,
                            it.isAddedToFavorites,
                            it.isAddedToWatched
                        )
                    }
                }
            }
        }

        lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingStateFlow.collect { isLoading ->
                    if (isLoading && loadingDialog == null && supportFragmentManager.findFragmentByTag(Constants.LOADING_DIALOG_TAG) == null) {
                        loadingDialog = LoadingDialog()
                        loadingDialog?.show(supportFragmentManager, Constants.LOADING_DIALOG_TAG)
                    } else {
                        loadingDialog?.dismiss()
                        loadingDialog = null
                    }
                }
            }
        }
    }

    private fun initListeners() = with(binding) {
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this@MovieDetailsActivity, MainActivity::class.java))
            finish()
        }
        val movieId = intent.getIntExtra(MOVIE_ID, -1)

        with(actions) {
            watchLaterButton.setOnClickListener {
                if (it.isSelected) {
                    removeFromWatchLater(movieId)
                } else {
                    addToWatchLater(movieId)
                }
            }

            favoritesButton.setOnClickListener {
                if (it.isSelected) {
                    removeFromFavorites(movieId)
                } else {
                    addToFavorites(movieId)
                }
            }

            watchedButton.setOnClickListener {
                if (it.isSelected) {
                    removeFromWatched(movieId)
                } else {
                    addToWatched(movieId)
                }
            }
        }
    }

    private fun addToWatchLater(movieId: Int) {
        viewModel.addToWatchlist(movieId)
        Toast.makeText(this, "Added to Watch Later", Toast.LENGTH_SHORT).show()
    }

    private fun addToFavorites(movieId: Int) {
        viewModel.addToFavorites(movieId)
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
    }

    private fun addToWatched(movieId: Int) {
        viewModel.addToWatched(movieId)
        Toast.makeText(this, "Added to Watched", Toast.LENGTH_SHORT).show()
    }

    private fun removeFromWatchLater(movieId: Int) {
        viewModel.removeFromWatchlist(movieId)
        Toast.makeText(this, "Removed from Watch Later", Toast.LENGTH_SHORT).show()
    }

    private fun removeFromFavorites(movieId: Int) {
        viewModel.removeFromFavorites(movieId)
        Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
    }

    private fun removeFromWatched(movieId: Int) {
        viewModel.removeFromWatched(movieId)
        Toast.makeText(this, "Removed from Watched", Toast.LENGTH_SHORT).show()
    }

    private fun initUi(movieDetails: MovieDetails, addedToWatchLater: Boolean, addedToFavorites: Boolean, addedToWatched: Boolean) =
        with(binding) {
            title.text = movieDetails.title
            toolbar.title = movieDetails.title
            overview.text = movieDetails.overview
            releaseDate.text = movieDetails.releaseDate
            rating.text = movieDetails.rating.toString()
            genres.text = movieDetails.genres.joinToString(", ")
            spokenLanguages.text = movieDetails.spokenLanguages.joinToString(", ")
            productionCountries.text = movieDetails.productionCountries.joinToString(", ")
            duration.text = movieDetails.duration
            image.load(movieDetails.posterImage)
            with(actions) {
                favoritesButton.isSelected = addedToFavorites
                watchLaterButton.isSelected = addedToWatchLater
                watchedButton.isSelected = addedToWatched
            }
        }
}
