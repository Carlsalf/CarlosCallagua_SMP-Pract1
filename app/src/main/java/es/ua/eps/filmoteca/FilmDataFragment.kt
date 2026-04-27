// app/src/main/java/es/ua/eps/filmoteca/FilmDataFragment.kt
package es.ua.eps.filmoteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class FilmDataFragment : Fragment() {

    companion object {
        private const val ARG_FILM_INDEX = "film_index"

        // Para móviles: crear fragment con argumento
        fun newInstance(index: Int): FilmDataFragment {
            return FilmDataFragment().apply {
                arguments = bundleOf(ARG_FILM_INDEX to index)
            }
        }
    }

    private var currentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentIndex = savedInstanceState?.getInt(ARG_FILM_INDEX)
            ?: arguments?.getInt(ARG_FILM_INDEX)
                    ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_film_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Mostrar la peli inicial
        showFilm(currentIndex)
    }

    //  Método público para tablets (Ejercicio 2)
    fun showFilm(index: Int) {
        currentIndex = index
        if (!isAdded) return   // por si llaman antes de que exista la vista

        val film = FilmDataSource.films[index]

        val imgPoster   = view?.findViewById<ImageView>(R.id.imagePoster)
        val txtTitle    = view?.findViewById<TextView>(R.id.textTitle)
        val txtDirector = view?.findViewById<TextView>(R.id.textDirector)
        val txtYear     = view?.findViewById<TextView>(R.id.textYear)
        val txtGenre    = view?.findViewById<TextView>(R.id.textGenre)
        val txtFormat   = view?.findViewById<TextView>(R.id.textFormat)
        val txtImdb     = view?.findViewById<TextView>(R.id.textImdb)
        val txtNotes    = view?.findViewById<TextView>(R.id.textNotes)

        imgPoster?.setImageResource(film.imageResId)
        txtTitle?.text    = film.title
        txtDirector?.text = film.director
        txtYear?.text     = film.year.toString()
        txtGenre?.text    = film.genre.toString()
        txtFormat?.text   = film.format.toString()
        txtImdb?.text     = film.imdbUrl

        // Tu clase Film no tiene "notes", así que dejamos un texto fijo o vacío
        txtNotes?.text    = ""   // o "Sin notas adicionales"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_FILM_INDEX, currentIndex)
    }
}
