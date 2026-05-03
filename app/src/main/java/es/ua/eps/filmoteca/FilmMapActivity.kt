package es.ua.eps.filmoteca

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class FilmMapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "FilmMapActivity"

        // 🔥 fallback REAL (Los Angeles)
        private val DEFAULT_LOCATION = LatLng(34.0522, -118.2437)
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var filmTitle = "Película"
    private var filmDirector = "Director"
    private var filmYear = 0

    private var latitude = 0.0
    private var longitude = 0.0

    private lateinit var filmPosition: LatLng

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) enableLocation()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_map)

        supportActionBar?.title = "Lugar de rodaje"

        // 🔹 DATA
        filmTitle = intent.getStringExtra("title") ?: filmTitle
        filmDirector = intent.getStringExtra("director") ?: filmDirector
        filmYear = intent.getIntExtra("year", 0)

        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        // 🔥 VALIDACIÓN CLAVE
        filmPosition = if (latitude == 0.0 && longitude == 0.0) {
            Log.w(TAG, "Coordenadas inválidas → usando fallback")
            DEFAULT_LOCATION
        } else {
            LatLng(latitude, longitude)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFilm) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.clear()

        // 🔥 FORZAR TIPO MAPA (evita grid)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // 🔥 MARCADOR
        googleMap.addMarker(
            MarkerOptions()
                .position(filmPosition)
                .title(filmTitle)
                .snippet("$filmDirector - $filmYear")
        )?.showInfoWindow()

        // 🔥 ZOOM SEGURO
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(filmPosition, 14f)
        )

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        checkPermission()
    }

    private fun checkPermission() {
        val granted =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED

        if (granted) enableLocation()
        else requestPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                showBounds(it)
            }
        }
    }

    private fun showBounds(location: Location) {
        val user = LatLng(location.latitude, location.longitude)

        val bounds = LatLngBounds.builder()
            .include(filmPosition)
            .include(user)
            .build()

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(bounds, 120)
        )
    }
}