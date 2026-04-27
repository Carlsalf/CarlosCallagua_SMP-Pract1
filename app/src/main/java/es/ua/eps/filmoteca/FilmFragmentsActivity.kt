// app/src/main/java/es/ua/eps/filmoteca/FilmFragmentsActivity.kt
package es.ua.eps.filmoteca

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class FilmFragmentsActivity :
    AppCompatActivity(),
    FilmListFragment.OnFilmSelectedListener {

    // true = tablet / layout sw600dp (dos paneles)
    private val isTwoPane: Boolean
        get() = findViewById<View?>(R.id.detailFragmentContainer) != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_fragments)

        // Versión pantallas pequeñas: cargamos la lista en el contenedor único
        if (!isTwoPane && savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,       // id del root en res/layout/
                    FilmListFragment()
                )
                .commit()
        }
        // En pantallas grandes los fragments ya están puestos en el XML
        // (FilmListFragment y FilmDataFragment dentro de layout-sw600dp/)
    }

    // ===== Ejercicio 2: reacción al click en la lista =====
    override fun onFilmSelected(position: Int) {
        if (isTwoPane) {
            // TABLET: actualizamos el fragment de detalle ya existente
            val detailFragment =
                supportFragmentManager.findFragmentById(R.id.detailFragmentContainer)
                        as? FilmDataFragment
            detailFragment?.showFilm(position)
        } else {
            // MÓVIL: navegamos al fragment de detalle
            val fragment = FilmDataFragment.newInstance(position)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
