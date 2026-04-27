package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FilmDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usa el layout que tengas para el detalle. Cambia si el tuyo es otro.
        setContentView(R.layout.activity_film_data)

        val imgPoster: ImageView = findViewById(R.id.imgPoster)
        val txtTitle: TextView = findViewById(R.id.txtTitle)
        val txtDirector: TextView = findViewById(R.id.txtDirector)
        val txtNotes: TextView = findViewById(R.id.txtNotes)
        val btnImdb: Button = findViewById(R.id.btnImdb)

        val index = intent.getIntExtra("film_index", -1)
        val film: Film? =
            if (index in 0 until FilmDataSource.films.size) FilmDataSource.films[index] else null

        val posterResId = film?.imageResId ?: R.mipmap.ic_launcher
        val title = film?.title ?: getString(R.string.sample_title)
        val directorName = film?.director
            ?: getString(R.string.sample_director).removePrefix("Dirigida por: ").trim()
        val year = film?.year ?: 1982
        val genreIndex = film?.genre ?: Film.GENRE_SCIFI
        val formatIndex = film?.format ?: Film.FORMAT_BLURAY
        val imdbUrl = film?.imdbUrl ?: getString(R.string.sample_imdb)
        val notes = film?.comments ?: getString(R.string.sample_notes)

        imgPoster.setImageResource(posterResId)
        txtTitle.text = title

        val genres = resources.getStringArray(R.array.genres)
        val formats = resources.getStringArray(R.array.formats)
        val line1 = getString(R.string.directed_by, directorName)
        val line2 = getString(
            R.string.year_genre_format,
            year,
            genres.getOrNull(genreIndex) ?: "-",
            formats.getOrNull(formatIndex) ?: "-"
        )
        txtDirector.text = "$line1\n$line2"
        txtNotes.text = notes

        btnImdb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl)))
        }
    }
}
