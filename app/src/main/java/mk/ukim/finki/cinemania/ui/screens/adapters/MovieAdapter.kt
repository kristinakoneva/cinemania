package mk.ukim.finki.cinemania.ui.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import mk.ukim.finki.cinemania.databinding.ItemMovieBinding
import mk.ukim.finki.cinemania.domain.models.Movie

class MovieAdapter(
    private var items: List<Movie>,
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

    fun submitList(movies: List<Movie>) {
        items = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            with(binding) {
                movieTitle.text = item.title
                movieImage.load(item.posterImage)
                root.setOnClickListener {
                    onItemClickCallback(item)
                }
            }
        }
    }
}