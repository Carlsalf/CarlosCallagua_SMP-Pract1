// app/src/main/java/es/ua/eps/filmoteca/FilmListFragment.kt
package es.ua.eps.filmoteca

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.ListFragment

class FilmListFragment : ListFragment() {

    // ==== Callback hacia la Activity ====
    interface OnFilmSelectedListener {
        fun onFilmSelected(position: Int)
    }

    private var listener: OnFilmSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // La Activity debe implementar OnFilmSelectedListener
        listener = context as? OnFilmSelectedListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usamos el mismo adapter que en la lista XML
        val adapter = FilmListAdapter(
            requireContext(),
            FilmDataSource.films
        )
        listAdapter = adapter
    }

    // Cuando se pulsa una película
    override fun onListItemClick(
        l: ListView,
        v: View,
        position: Int,
        id: Long
    ) {
        super.onListItemClick(l, v, position, id)

        val film = FilmDataSource.films[position]
        // Un pequeño Toast (para ver que funciona)
        Toast.makeText(
            requireContext(),
            "${film.title} (${film.director})",
            Toast.LENGTH_SHORT
        ).show()

        // Avisamos a la Activity (FilmFragmentsActivity)
        listener?.onFilmSelected(position)
    }
}
