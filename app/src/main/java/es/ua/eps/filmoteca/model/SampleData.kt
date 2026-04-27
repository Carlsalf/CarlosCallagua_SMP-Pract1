// app/src/main/java/es/ua/eps/filmoteca/model/SampleData.kt
package es.ua.eps.filmoteca.model

import es.ua.eps.filmoteca.Film
import es.ua.eps.filmoteca.R

object SampleData {
    /** Devuelve una lista de pelis de ejemplo (mutable, por si luego editas). */
    fun films(): MutableList<Film> = mutableListOf(
        Film(
            imageResId = R.mipmap.ic_launcher,
            title = "Regreso al futuro",
            director = "Robert Zemeckis",
            year = 1985,
            genre = Film.GENRE_SCIFI,
            format = Film.FORMAT_ONLINE,
            imdbUrl = "https://www.imdb.com/title/tt0088763/",
            comments = ""
        ),
        Film(
            imageResId = R.drawable.carlosalfredo, // tu drawable existente
            title = "Blade Runner",
            director = "Ridley Scott",
            year = 1982,
            genre = Film.GENRE_SCIFI,
            format = Film.FORMAT_BLURAY,
            imdbUrl = "https://www.imdb.com/title/tt0083658/",
            comments = "Neo-noir cyberpunk"
        )
    )
}
