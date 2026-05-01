package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FilmMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private var filmTitle: String = "Película"
    private var filmDirector: String = "Director desconocido"
    private var filmYear: Int = 0
    private var latitude: Double = 38.3452
    private var longitude: Double = -0.4810

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_map)

        filmTitle = intent.getStringExtra("title") ?: "Película"
        filmDirector = intent.getStringExtra("director") ?: "Director desconocido"
        filmYear = intent.getIntExtra("year", 0)
        latitude = intent.getDoubleExtra("latitude", 38.3452)
        longitude = intent.getDoubleExtra("longitude", -0.4810)

        supportActionBar?.title = "Lugar de rodaje"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFilm) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val position = LatLng(latitude, longitude)

        googleMap.addMarker(
            MarkerOptions()
                .position(position)
                .title(filmTitle)
                .snippet("$filmDirector - $filmYear")
        )?.showInfoWindow()

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(position, 14f)
        )

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
    }
}