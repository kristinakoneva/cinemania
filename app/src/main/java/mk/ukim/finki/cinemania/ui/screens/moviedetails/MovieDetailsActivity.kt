package mk.ukim.finki.cinemania.ui.screens.moviedetails

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.cinemania.R
import mk.ukim.finki.cinemania.databinding.ActivityMovieDetailsBinding
import mk.ukim.finki.cinemania.domain.models.MovieDetails
import mk.ukim.finki.cinemania.extensions.viewBinding
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

        val addToWatchLaterButton = findViewById<MaterialButton>(R.id.addToWatchLaterButton)
        val addToFavoritesButton = findViewById<MaterialButton>(R.id.addToFavoritesButton)
        val addToWatchedButton = findViewById<MaterialButton>(R.id.addToWatchedButton)

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
                        initUi(it.movieDetails)
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
        toolbar.setNavigationOnClickListener { finish() }
        val movieId = intent.getIntExtra(MOVIE_ID, -1)

        addToWatchLaterButton.setOnClickListener {
            addToWatchLater(movieId)
        }

        addToFavoritesButton.setOnClickListener {
            addToFavorites(movieId)
        }

        addToWatchedButton.setOnClickListener {
            addToWatched(movieId)
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

    private fun initUi(movieDetails: MovieDetails) = with(binding) {
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
    }
}
