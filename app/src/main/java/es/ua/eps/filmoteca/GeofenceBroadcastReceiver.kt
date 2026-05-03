package es.ua.eps.filmoteca

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "GeofenceReceiver"
        private const val CHANNEL_ID = "geofence_channel"
        private const val CHANNEL_NAME = "Geocercas Filmoteca"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent) ?: return

        if (event.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(event.errorCode)
            Log.e(TAG, "Error en geofence: $errorMessage")
            return
        }

        val transition = event.geofenceTransition

        if (
            transition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            transition == Geofence.GEOFENCE_TRANSITION_EXIT
        ) {
            val filmTitle = event.triggeringGeofences
                ?.firstOrNull()
                ?.requestId
                ?: "Lugar de rodaje"

            val message = when (transition) {
                Geofence.GEOFENCE_TRANSITION_ENTER ->
                    "Has entrado en la zona de: $filmTitle"

                Geofence.GEOFENCE_TRANSITION_EXIT ->
                    "Has salido de la zona de: $filmTitle"

                else -> "Evento de geocerca detectado"
            }

            Log.d(TAG, message)

            showNotification(
                context = context,
                title = "Geocerca Filmoteca",
                message = message
            )
        }
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}