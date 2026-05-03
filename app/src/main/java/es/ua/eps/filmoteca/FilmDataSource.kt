package es.ua.eps.filmoteca

import android.util.Log

object FilmDataSource {

    private const val TAG = "FilmDataSource"

    private fun posterOrDefault(tryRes: Int): Int =
        runCatching { tryRes }.getOrElse { R.mipmap.ic_launcher }

    val films: MutableList<Film> = mutableListOf(
        Film(
            imageResId = posterOrDefault(R.drawable.poster_back_to_the_future),
            title = "Regreso al futuro",
            director = "Robert Zemeckis",
            year = 1985,
            genre = Film.GENRE_SCIFI,
            format = Film.FORMAT_ONLINE,
            imdbUrl = "https://www.imdb.com/title/tt0088763/",
            comments = "Lugar de rodaje: Courthouse Square, Universal Studios.",
            latitude = 34.1419,
            longitude = -118.3534
        ),
        Film(
            imageResId = posterOrDefault(R.drawable.poster_blade_runner),
            title = "Blade Runner",
            director = "Ridley Scott",
            year = 1982,
            genre = Film.GENRE_SCIFI,
            format = Film.FORMAT_BLURAY,
            imdbUrl = "https://www.imdb.com/title/tt0083658/",
            comments = "Lugar de rodaje: Bradbury Building, Los Ángeles.",
            latitude = 34.0506,
            longitude = -118.2478
        ),
        Film(
            imageResId = posterOrDefault(R.drawable.poster_matrix),
            title = "The Matrix",
            director = "Lana & Lilly Wachowski",
            year = 1999,
            genre = Film.GENRE_SCIFI,
            format = Film.FORMAT_DVD,
            imdbUrl = "https://www.imdb.com/title/tt0133093/",
            comments = "Lugar de rodaje: Downtown Los Ángeles.",
            latitude = 34.0522,
            longitude = -118.2437
        ),
        Film(
            imageResId = posterOrDefault(R.drawable.poster_godfather),
            title = "El Padrino",
            director = "Francis Ford Coppola",
            year = 1972,
            genre = Film.GENRE_DRAMA,
            format = Film.FORMAT_BLURAY,
            imdbUrl = "https://www.imdb.com/title/tt0068646/",
            comments = "Lugar de rodaje de demostración: Los Ángeles.",
            latitude = 34.0736,
            longitude = -118.2400
        ),
        Film(
            imageResId = posterOrDefault(R.drawable.poster_toy_story),
            title = "Toy Story",
            director = "John Lasseter",
            year = 1995,
            genre = Film.GENRE_COMEDY,
            format = Film.FORMAT_ONLINE,
            imdbUrl = "https://www.imdb.com/title/tt0114709/",
            comments = "Lugar de referencia: Pixar Animation Studios.",
            latitude = 37.8324,
            longitude = -122.2841
        )
    )

    fun addOrUpdateFilm(
        title: String,
        director: String = "Desconocido",
        year: Int = 2024,
        genre: Int = Film.GENRE_SCIFI,
        format: Int = Film.FORMAT_ONLINE,
        imdbUrl: String = "",
        imageResId: Int = R.mipmap.ic_launcher,
        comments: String = "Película recibida mediante Firebase Cloud Messaging",
        latitude: Double = 34.0522,
        longitude: Double = -118.2437
    ): Boolean {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return false

        val existing = films.firstOrNull {
            it.title.equals(cleanTitle, ignoreCase = true)
        }

        return if (existing != null) {
            existing.director = director
            existing.year = year
            existing.genre = genre
            existing.format = format
            existing.imdbUrl = imdbUrl
            existing.imageResId = imageResId
            existing.comments = comments
            existing.latitude = latitude
            existing.longitude = longitude

            Log.d(TAG, "Película actualizada: $cleanTitle")
            false
        } else {
            films.add(
                Film(
                    imageResId = imageResId,
                    title = cleanTitle,
                    director = director,
                    year = year,
                    genre = genre,
                    format = format,
                    imdbUrl = imdbUrl,
                    comments = comments,
                    latitude = latitude,
                    longitude = longitude
                )
            )

            Log.d(TAG, "Película añadida: $cleanTitle")
            true
        }
    }

    fun deleteFilmByTitle(title: String): Boolean {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return false

        val removed = films.removeAll {
            it.title.equals(cleanTitle, ignoreCase = true)
        }

        if (removed) {
            Log.d(TAG, "Película eliminada: $cleanTitle")
        } else {
            Log.d(TAG, "Película no encontrada para eliminar: $cleanTitle")
        }

        return removed
    }
}