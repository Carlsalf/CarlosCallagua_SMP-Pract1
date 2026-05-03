package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FilmDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_data)

        val imgPoster: ImageView = findViewById(R.id.imgPoster)
        val txtTitle: TextView = findViewById(R.id.txtTitle)
        val txtDir: TextView = findViewById(R.id.txtDirector)
        val txtNotes: TextView = findViewById(R.id.txtNotes)
        val btnImdb: Button = findViewById(R.id.btnImdb)
        val btnMap: Button = findViewById(R.id.btnMap)

        val index = intent.getIntExtra("film_index", 0)
        val film = FilmDataSource.films.getOrNull(index) ?: Film()

        imgPoster.setImageResource(
            if (film.imageResId != 0) film.imageResId else R.mipmap.ic_launcher
        )

        txtTitle.text = film.title.ifBlank { "<Sin título>" }
        txtDir.text = "Dirigida por: ${film.director}\nAño: ${film.year}"
        txtNotes.text = film.comments

        btnImdb.setOnClickListener {
            val url = film.imdbUrl.ifBlank { "https://www.imdb.com/" }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        btnMap.setOnClickListener {
            val intent = Intent(this, FilmMapActivity::class.java).apply {
                putExtra("title", film.title)
                putExtra("director", film.director)
                putExtra("year", film.year)
                putExtra("latitude", film.latitude)
                putExtra("longitude", film.longitude)
            }

            startActivity(intent)
        }
    }
}