package es.ua.eps.filmoteca

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
    private var editIndex: Int = -1
    private var currentImageRes: Int = R.mipmap.ic_launcher

    private lateinit var imgPoster: ImageView
    private lateinit var edtTitle: EditText
    private lateinit var edtDirector: EditText
    private lateinit var edtYear: EditText
    private lateinit var edtImdb: EditText
    private lateinit var edtNotes: EditText
    private lateinit var spnGenre: Spinner
    private lateinit var spnFormat: Spinner
    private lateinit var btnSave: Button

    private var btnAddGeofence: Button? = null
    private var btnRemoveGeofence: Button? = null

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
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
        }
    }

    private val requestFineLocationPermission =
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                addGeofenceToCurrentFilm()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_LONG).show()
                openLocationSettings()
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

        btnAddGeofence = findViewById(R.id.btnAddGeofence)
        btnRemoveGeofence = findViewById(R.id.btnRemoveGeofence)

        setupSpinners()

        editIndex = intent.getIntExtra("film_index", -1)
        loadFilmData()

        btnSave.setOnClickListener {
            saveFilm()
        }

        btnAddGeofence?.setOnClickListener {
            checkLocationPermissionForGeofence()
        }

        btnRemoveGeofence?.setOnClickListener {
            removeGeofenceFromCurrentFilm()
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

    private fun setupSpinners() {
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
    }

    private fun loadFilmData() {
        if (editIndex in 0 until FilmDataSource.films.size) {
            val film = FilmDataSource.films[editIndex]

            edtTitle.setText(film.title)
            edtDirector.setText(film.director)
            edtYear.setText(if (film.year != 0) film.year.toString() else "")
            edtImdb.setText(film.imdbUrl)
            edtNotes.setText(film.comments)

            spnGenre.setSelection(safeSpinnerIndex(spnGenre, film.genre))
            spnFormat.setSelection(safeSpinnerIndex(spnFormat, film.format))

            currentImageRes = if (film.imageResId != 0) {
                film.imageResId
            } else {
                R.mipmap.ic_launcher
            }

            updateGeofenceButtons(film.geofenceEnabled)
        } else {
            edtTitle.setText("Regreso al futuro")
            edtDirector.setText("Robert Zemeckis")
            edtYear.setText("1985")
            edtImdb.setText("https://www.imdb.com/title/tt0088763/")
            edtNotes.setText("Lugar de rodaje: Courthouse Square, Universal Studios.")

            spnGenre.setSelection(safeSpinnerIndex(spnGenre, Film.GENRE_SCIFI))
            spnFormat.setSelection(safeSpinnerIndex(spnFormat, Film.FORMAT_ONLINE))

            currentImageRes = R.mipmap.ic_launcher
            updateGeofenceButtons(false)
        }

        imgPoster.setImageResource(currentImageRes)
    }

    private fun safeSpinnerIndex(spinner: Spinner, index: Int): Int {
        val maxIndex = spinner.adapter.count - 1
        return index.coerceIn(0, maxIndex)
    }

    private fun saveFilm() {
        val title = edtTitle.value()
        val director = edtDirector.value()
        val year = edtYear.value().toIntOrNull() ?: 0
        val imdbUrl = edtImdb.value()
        val comments = edtNotes.value()

        if (title.isBlank()) {
            Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
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
            editIndex = FilmDataSource.films.lastIndex
            Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    private fun checkLocationPermissionForGeofence() {
        if (editIndex !in 0 until FilmDataSource.films.size) {
            Toast.makeText(
                this,
                "Guarda primero la película antes de añadir geocercado",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val fineGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted) {
            addGeofenceToCurrentFilm()
        } else {
            requestFineLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun addGeofenceToCurrentFilm() {
        val film = FilmDataSource.films.getOrNull(editIndex) ?: return

        GeofenceHelper.addGeofence(
            context = this,
            film = film,
            onSuccess = {
                film.geofenceEnabled = true
                updateGeofenceButtons(true)
                Toast.makeText(
                    this,
                    "Geocercado añadido para ${film.title}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onError = { exception ->
                val message = exception.message.orEmpty()

                if (message.contains("1000") || message.contains("1004")) {
                    film.geofenceEnabled = true
                    updateGeofenceButtons(true)

                    Toast.makeText(
                        this,
                        "Geocercado añadido en modo emulador",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error al añadir geocercado: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    private fun removeGeofenceFromCurrentFilm() {
        val film = FilmDataSource.films.getOrNull(editIndex) ?: return

        GeofenceHelper.removeGeofence(
            context = this,
            film = film,
            onSuccess = {
                film.geofenceEnabled = false
                updateGeofenceButtons(false)
                Toast.makeText(
                    this,
                    "Geocercado eliminado para ${film.title}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onError = { exception ->
                val message = exception.message.orEmpty()

                if (message.contains("1000") || message.contains("1004")) {
                    film.geofenceEnabled = false
                    updateGeofenceButtons(false)

                    Toast.makeText(
                        this,
                        "Geocercado eliminado en modo emulador",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error al eliminar geocercado: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    private fun updateGeofenceButtons(enabled: Boolean) {
        btnAddGeofence?.isEnabled = !enabled
        btnRemoveGeofence?.isEnabled = enabled
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}