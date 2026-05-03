// app/src/main/java/es/ua/eps/filmoteca/FilmEditActivity.kt
package es.ua.eps.filmoteca

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FilmEditActivity : AppCompatActivity() {

    private var capturedUri: Uri? = null

    private lateinit var imgPoster: ImageView
    private lateinit var edtTitle: EditText
    private lateinit var edtDirector: EditText
    private lateinit var edtYear: EditText
    private lateinit var edtImdb: EditText
    private lateinit var edtNotes: EditText
    private lateinit var spnGenre: Spinner
    private lateinit var spnFormat: Spinner
    private lateinit var btnSave: Button

    private fun EditText.value(): String = text?.toString()?.trim() ?: ""

    private val pickImage = registerForActivityResult(GetContent()) { uri: Uri? ->
        if (uri != null) {
            capturedUri = uri
            imgPoster.setImageURI(uri)
        } else {
            Toast.makeText(this, "No se seleccionó imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePhoto = registerForActivityResult(TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            imgPoster.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestCameraPermission = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            takePhoto.launch(null)
        } else {
            Toast.makeText(
                this,
                "Permiso de cámara denegado. No se puede tomar la fotografía.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_edit)

        imgPoster = findViewById(R.id.imgPosterEdit)
        edtTitle = findViewById(R.id.edtTitle)
        edtDirector = findViewById(R.id.edtDirector)
        edtYear = findViewById(R.id.edtYear)
        edtImdb = findViewById(R.id.edtImdb)
        edtNotes = findViewById(R.id.edtNotes)
        spnGenre = findViewById(R.id.spnGenre)
        spnFormat = findViewById(R.id.spnFormat)
        btnSave = findViewById(R.id.btnSave)

        spnGenre.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_dropdown_item
        )

        spnFormat.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.formats,
            android.R.layout.simple_spinner_dropdown_item
        )

        val editIndex = intent.getIntExtra("film_index", -1)
        var currentImageRes = R.mipmap.ic_launcher

        if (editIndex in 0 until FilmDataSource.films.size) {
            val f = FilmDataSource.films[editIndex]

            edtTitle.setText(f.title)
            edtDirector.setText(f.director)
            edtYear.setText(if (f.year != 0) f.year.toString() else "")
            edtImdb.setText(f.imdbUrl)
            edtNotes.setText(f.comments)

            spnGenre.setSelection(f.genre.coerceAtLeast(0))
            spnFormat.setSelection(f.format.coerceAtLeast(0))

            currentImageRes = if (f.imageResId != 0) f.imageResId else R.mipmap.ic_launcher
        } else {
            edtTitle.setText("Regreso al futuro")
            edtDirector.setText("Robert Zemeckis")
            edtYear.setText("1985")
            edtImdb.setText("https://www.imdb.com/title/tt0088763/")
            edtNotes.setText("Lugar de rodaje: Courthouse Square, Universal Studios.")
            spnGenre.setSelection(Film.GENRE_SCIFI)
            spnFormat.setSelection(Film.FORMAT_ONLINE)
        }

        imgPoster.setImageResource(currentImageRes)

        btnSave.setOnClickListener {
            val title = edtTitle.value()
            val director = edtDirector.value()
            val year = edtYear.value().toIntOrNull() ?: 0
            val imdbUrl = edtImdb.value()
            val comments = edtNotes.value()

            if (title.isBlank()) {
                Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val oldFilm = FilmDataSource.films.getOrNull(editIndex)

            val film = Film(
                imageResId = currentImageRes,
                title = title,
                director = director,
                year = year,
                genre = spnGenre.selectedItemPosition,
                format = spnFormat.selectedItemPosition,
                imdbUrl = imdbUrl,
                comments = comments,
                latitude = oldFilm?.latitude ?: 34.1419,
                longitude = oldFilm?.longitude ?: -118.3534,
                geofenceEnabled = oldFilm?.geofenceEnabled ?: false
            )

            if (editIndex in 0 until FilmDataSource.films.size) {
                FilmDataSource.films[editIndex] = film
                Toast.makeText(this, "Película actualizada", Toast.LENGTH_SHORT).show()
            } else {
                FilmDataSource.films.add(film)
                Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show()
            }

            finish()
        }

        findViewById<Button>(R.id.btnTakePhoto).setOnClickListener {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                takePhoto.launch(null)
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }

        findViewById<Button>(R.id.btnPickImage).setOnClickListener {
            pickImage.launch("image/*")
        }
    }
}