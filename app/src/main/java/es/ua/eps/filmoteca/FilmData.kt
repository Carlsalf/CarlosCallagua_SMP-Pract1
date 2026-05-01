package es.ua.eps.filmoteca

object FilmData {

    val films: MutableList<Film> = mutableListOf(

        Film(
            imageResId = R.mipmap.ic_launcher,
            title = "The Matrix",
            director = "Wachowski",
            year = 1999,
            genre = Film.GENRE_SCIFI,
            format = Film.FORMAT_ONLINE,
            latitude = 37.7749,
            longitude = -122.4194
        ),

        Film(
            imageResId = R.mipmap.ic_launcher,
            title = "Interstellar",
            director = "Christopher Nolan",
            year = 2014,
            genre = Film.GENRE_SCIFI,
            format = Film.FORMAT_BLURAY,
            latitude = 31.9686,
            longitude = -99.9018
        ),

        Film(
            imageResId = R.mipmap.ic_launcher,
            title = "El Padrino",
            director = "Francis Ford Coppola",
            year = 1972,
            genre = Film.GENRE_DRAMA,
            format = Film.FORMAT_DVD,
            latitude = 40.7128,
            longitude = -74.0060
        )
    )
}