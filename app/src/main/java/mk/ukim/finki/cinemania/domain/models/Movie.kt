package mk.ukim.finki.cinemania.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val posterImage: String?
): Parcelable
