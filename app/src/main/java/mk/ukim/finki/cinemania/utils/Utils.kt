package mk.ukim.finki.cinemania.utils

object Utils {

    fun createImageUrl(imagePath: String?): String? =
        imagePath?.let {
            "https://image.tmdb.org/t/p/w500$imagePath"
        }
}