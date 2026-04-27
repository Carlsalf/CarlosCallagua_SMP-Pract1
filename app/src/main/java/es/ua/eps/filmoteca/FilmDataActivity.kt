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

        val imgPoster = findViewById<ImageView>(R.id.imgPoster)
        val txtTitle  = findViewById<TextView>(R.id.txtTitle)
        val txtDir    = findViewById<TextView>(R.id.txtDirector)
        val txtNotes  = findViewById<TextView>(R.id.txtNotes)
        val btnImdb   = findViewById<Button>(R.id.btnImdb)

        val index = intent.getIntExtra("film_index", 0)
        val film = FilmDataSource.films.getOrNull(index) ?: Film()

        imgPoster?.setImageResource(if (film.imageResId != 0) film.imageResId else R.mipmap.ic_launcher)
        txtTitle?.text = film.title ?: "<Sin título>"
        txtDir?.text   = "Dirigida por: ${film.director.orEmpty()}\nAño: ${film.year}"
        txtNotes?.text = film.comments.orEmpty()

        btnImdb?.setOnClickListener {
            val url = film.imdbUrl?.takeIf { it.isNotBlank() }
                ?: "https://www.imdb.com/"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}
