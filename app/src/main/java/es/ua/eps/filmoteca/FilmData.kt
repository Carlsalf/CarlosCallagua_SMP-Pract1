package es.ua.eps.filmoteca

object FilmData {
    // Lista global de películas que usan tanto el adapter como la activity
    val films: MutableList<Film> = mutableListOf(
        Film("Matrix", "Wachowski", 1999, 5),
        Film("Interstellar", "Christopher Nolan", 2014, 5),
        Film("El Padrino", "Francis Ford Coppola", 1972, 5)
    )
}
