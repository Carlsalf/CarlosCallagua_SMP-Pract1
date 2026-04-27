package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FilmListRvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_list_recycler)

        val recycler: RecyclerView = findViewById(R.id.recyclerFilms)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        val adapter = FilmRvAdapter(FilmDataSource.films) { position ->
            val i = Intent(this, FilmDataActivity::class.java)
            i.putExtra("film_index", position)
            startActivity(i)
        }
        recycler.adapter = adapter
    }
}
