package es.ua.eps.filmoteca

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

object GeofenceHelper {

    private const val GEOFENCE_RADIUS_METERS = 500f
    private const val PENDING_INTENT_REQUEST_CODE = 1001

    private var geofencingClient: GeofencingClient? = null

    private fun getClient(context: Context): GeofencingClient {
        if (geofencingClient == null) {
            geofencingClient = LocationServices.getGeofencingClient(context.applicationContext)
        }
        return geofencingClient!!
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java).apply {
            action = "es.ua.eps.filmoteca.ACTION_GEOFENCE_EVENT"
        }

        return PendingIntent.getBroadcast(
            context,
            PENDING_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    fun addGeofence(
        context: Context,
        film: Film,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onError(SecurityException("Permiso ACCESS_FINE_LOCATION no concedido"))
            return
        }

        val requestId = film.title?.takeIf { it.isNotBlank() } ?: "Película"

        val geofence = Geofence.Builder()
            .setRequestId(requestId)
            .setCircularRegion(
                film.latitude,
                film.longitude,
                GEOFENCE_RADIUS_METERS
            )
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER or
                        Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .setLoiteringDelay(30000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        getClient(context)
            .addGeofences(request, getPendingIntent(context))
            .addOnSuccessListener {
                film.geofenceEnabled = true
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun removeGeofence(
        context: Context,
        film: Film,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val requestId = film.title?.takeIf { it.isNotBlank() } ?: "Película"

        getClient(context)
            .removeGeofences(listOf(requestId))
            .addOnSuccessListener {
                film.geofenceEnabled = false
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}