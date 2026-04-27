package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class FilmListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: FilmListAdapter

    private val films: MutableList<Film> = FilmDataSource.films

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_list)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        toolbar.inflateMenu(R.menu.menu_film_list)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add_film -> {
                    addDefaultFilm()
                    true
                }

                R.id.action_about -> {
                    openAbout()
                    true
                }

                else -> false
            }
        }

        listView = findViewById(R.id.listFilms)
        adapter = FilmListAdapter(this, films)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val film = films[position]
            Toast.makeText(
                this,
                "${film.title} (${film.director})",
                Toast.LENGTH_SHORT
            ).show()
        }

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        listView.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.menu_film_list_context_delete, menu)
                mode.title = "0 seleccionadas"
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

            override fun onItemCheckedStateChanged(
                mode: ActionMode,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                val count = listView.checkedItemCount
                mode.title = "$count seleccionada" + if (count != 1) "s" else ""
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_delete_selected -> {
                        deleteSelectedFilms()
                        mode.finish()
                        true
                    }

                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                for (i in 0 until listView.count) {
                    listView.setItemChecked(i, false)
                }
            }
        })

        handleNotificationIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        val operation = intent?.getStringExtra("operation")
        val title = intent?.getStringExtra("title")

        if (!operation.isNullOrBlank() && !title.isNullOrBlank()) {
            Toast.makeText(this, "$operation: $title", Toast.LENGTH_LONG).show()
            adapter.notifyDataSetChanged()
        }
    }

    private fun addDefaultFilm() {
        FilmDataSource.addOrUpdateFilm(
            title = "Nueva película",
            director = "Desconocido",
            year = 2024,
            genre = Film.GENRE_ACTION,
            format = Film.FORMAT_ONLINE
        )

        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show()
    }

    private fun deleteSelectedFilms() {
        val checked = listView.checkedItemPositions
        val toRemove = mutableListOf<Film>()

        for (i in 0 until checked.size()) {
            val position = checked.keyAt(i)
            if (checked.valueAt(i)) {
                toRemove.add(films[position])
            }
        }

        films.removeAll(toRemove)
        adapter.notifyDataSetChanged()

        Toast.makeText(this, "Películas eliminadas", Toast.LENGTH_SHORT).show()
    }

    private fun openAbout() {
        startActivity(Intent(this, AboutActivity::class.java))
    }
}