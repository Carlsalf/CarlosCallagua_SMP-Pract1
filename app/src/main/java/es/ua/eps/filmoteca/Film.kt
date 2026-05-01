package es.ua.eps.filmoteca

data class Film(
    var imageResId: Int = R.drawable.ic_launcher_foreground,
    var title: String? = null,
    var director: String? = null,
    var year: Int = 0,
    var genre: Int = GENRE_ACTION,
    var format: Int = FORMAT_DVD,
    var imdbUrl: String? = null,
    var comments: String? = null,

    // Práctica Tema 3 - mapas/localización
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var geofenceEnabled: Boolean = false
) {
    companion object {
        const val GENRE_ACTION = 0
        const val GENRE_COMEDY = 1
        const val GENRE_DRAMA = 2
        const val GENRE_SCIFI = 3

        const val FORMAT_DVD = 0
        const val FORMAT_BLURAY = 1
        const val FORMAT_DIGITAL = 2
        const val FORMAT_ONLINE = 3
    }
}