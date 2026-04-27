package es.ua.eps.filmoteca

import java.io.Serializable

data class Film(
    var title: String? = null,
    var director: String? = null,
    var year: Int = 0,
    var genre: Int = 0,
    var format: Int = 0,
    var imdbUrl: String? = null,
    var imageResId: Int = 0,
    var comments: String? = null
) : Serializable {

    companion object {
        const val GENRE_SCIFI = 0
        const val GENRE_DRAMA = 1
        const val GENRE_COMEDY = 2
        const val GENRE_ACTION = 3

        const val FORMAT_DVD = 0
        const val FORMAT_BLURAY = 1
        const val FORMAT_ONLINE = 2
    }
}