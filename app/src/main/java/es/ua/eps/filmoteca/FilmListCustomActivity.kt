package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityFilmListCustomBinding

class FilmListCustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListCustomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = FilmListAdapter(this, FilmDataSource.films)
        binding.listViewCustom.adapter = adapter

        binding.listViewCustom.setOnItemClickListener { _, _, position, _ ->
            val i = Intent(this, FilmDataActivity::class.java)
            i.putExtra("film_index", position)
            startActivity(i)
        }
    }
}
