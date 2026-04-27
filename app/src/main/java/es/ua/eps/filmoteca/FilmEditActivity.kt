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

    // === Estado ===
    private var capturedUri: Uri? = null

    // === Views ===
    private lateinit var imgPoster: ImageView
    private lateinit var edtTitle: EditText
    private lateinit var edtDirector: EditText
    private lateinit var edtYear: EditText
    private lateinit var edtImdb: EditText
    private lateinit var edtNotes: EditText
    private lateinit var spnGenre: Spinner
    private lateinit var spnFormat: Spinner
    private lateinit var btnSave: Button

    // Elegir imagen de la galería
    private val pickImage = registerForActivityResult(GetContent()) { uri: Uri? ->
        if (uri != null) {
            capturedUri = uri
            imgPoster.setImageURI(uri)
        } else {
            Toast.makeText(this, "No se seleccionó imagen", Toast.LENGTH_SHORT).show()
        }
    }

    // Tomar foto con la cámara: devuelve un Bitmap (miniatura), sin URIs ni ficheros
    private val takePhoto = registerForActivityResult(TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            imgPoster.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    // Pedir permiso de cámara
    private val requestCameraPermission = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            // Ahora que el usuario lo ha dado, lanzamos la cámara
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

        // --- Referencias ---
        imgPoster   = findViewById(R.id.imgPosterEdit)
        edtTitle    = findViewById(R.id.edtTitle)
        edtDirector = findViewById(R.id.edtDirector)
        edtYear     = findViewById(R.id.edtYear)
        edtImdb     = findViewById(R.id.edtImdb)
        edtNotes    = findViewById(R.id.edtNotes)
        spnGenre    = findViewById(R.id.spnGenre)
        spnFormat   = findViewById(R.id.spnFormat)
        btnSave     = findViewById(R.id.btnSave)

        // --- Spinners ---
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

        // --- Modo edición / alta ---
        val editIndex = intent.getIntExtra("film_index", -1)
        var currentImageRes = R.drawable.ic_launcher_foreground

        if (editIndex in 0 until FilmDataSource.films.size) {
            val f = FilmDataSource.films[editIndex]
            edtTitle.setText(f.title ?: "")
            edtDirector.setText(f.director ?: "")
            edtYear.setText(if (f.year != 0) f.year.toString() else "")
            edtImdb.setText(f.imdbUrl ?: "")
            edtNotes.setText(f.comments ?: "")
            spnGenre.setSelection(f.genre.coerceAtLeast(0))
            spnFormat.setSelection(f.format.coerceAtLeast(0))
            currentImageRes = if (f.imageResId != 0) f.imageResId else currentImageRes
        } else {
            // Valores demo
            edtTitle.setText("Regreso al futuro")
            edtDirector.setText("Robert Zemeckis")
            edtYear.setText("1985")
            edtImdb.setText("https://www.imdb.com/title/tt0088763/")
            spnGenre.setSelection(Film.GENRE_SCIFI)
            spnFormat.setSelection(Film.FORMAT_ONLINE)
        }

        // Imagen por defecto mientras no hay imagen elegida
        imgPoster.setImageResource(currentImageRes)

        // --- Guardar en memoria (demo) ---
        btnSave.setOnClickListener {
            val film = Film().apply {
                imageResId = currentImageRes
                title      = edtTitle.text?.toString()?.trim()
                director   = edtDirector.text?.toString()?.trim()
                year       = edtYear.text?.toString()?.toIntOrNull() ?: 0
                genre      = spnGenre.selectedItemPosition
                format     = spnFormat.selectedItemPosition
                imdbUrl    = edtImdb.text?.toString()?.trim()
                comments   = edtNotes.text?.toString()?.trim()
            }

            if (editIndex in 0 until FilmDataSource.films.size) {
                FilmDataSource.films[editIndex] = film
                Toast.makeText(this, "Película actualizada", Toast.LENGTH_SHORT).show()
            } else {
                FilmDataSource.films.add(film)
                Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        // --- Botones imagen ---
        findViewById<Button>(R.id.btnTakePhoto).setOnClickListener {
            // 1) Comprobamos si YA tenemos el permiso
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                // Ya tenemos permiso → lanzar cámara
                takePhoto.launch(null)
            } else {
                // Pedir permiso al usuario
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }

        findViewById<Button>(R.id.btnPickImage).setOnClickListener {
            pickImage.launch("image/*")
        }
    }
}
