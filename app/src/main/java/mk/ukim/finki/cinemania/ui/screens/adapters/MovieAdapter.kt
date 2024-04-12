package mk.ukim.finki.cinemania.ui.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import mk.ukim.finki.cinemania.databinding.ItemMovieBinding
import mk.ukim.finki.cinemania.domain.models.Movie

class MovieAdapter(
    private var items: List<MovieItem>,
    private var areActionsVisible: Boolean = true,
    private val onWatchLaterButtonClick: (Int, Boolean) -> Unit = { _, _ -> },
    private val onFavoriteButtonClick: (Int, Boolean) -> Unit = { _, _ -> },
    private val onWatchedButtonClick: (Int, Boolean) -> Unit = { _, _ -> },
    private val onItemClickCallback: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun submitList(movies: List<MovieItem>) {
        items = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieItem) {
            with(binding) {
                val layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                var marginParams = ViewGroup.MarginLayoutParams(layoutParams)
                if (!areActionsVisible) {
                    actions.root.visibility = ViewGroup.GONE
                    layoutParams.width = 500
                    marginParams = ViewGroup.MarginLayoutParams(layoutParams)
                    marginParams.bottomMargin = 0
                } else {
                    marginParams.bottomMargin = 30
                    with(actions) {
                        watchLaterButton.isSelected = item.isAddedToWatchLater ?: false
                        watchLaterButton.setOnClickListener {
                            onWatchLaterButtonClick.invoke(item.movie.id, it.isSelected)
                        }
                        favoritesButton.isSelected = item.isAddedToFavorites ?: false
                        favoritesButton.setOnClickListener {
                            onFavoriteButtonClick.invoke(item.movie.id, it.isSelected)
                        }
                        watchedButton.isSelected = item.isAddedToWatched ?: false
                        watchedButton.setOnClickListener {
                            onWatchedButtonClick.invoke(item.movie.id, it.isSelected)
                        }
                    }
                }
                marginParams.marginStart = 8
                marginParams.marginEnd = 8
                root.layoutParams = marginParams
                movieTitle.text = item.movie.title
                movieImage.load(item.movie.posterImage)
                root.setOnClickListener {
                    onItemClickCallback(item.movie)
                }
            }
        }
    }
}
