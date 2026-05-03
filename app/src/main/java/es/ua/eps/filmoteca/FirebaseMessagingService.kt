package es.ua.eps.filmoteca

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCM"
        private const val CHANNEL_ID = "filmoteca_channel"
        private const val CHANNEL_NAME = "Notificaciones Filmoteca"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Nuevo token FCM: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(TAG, "Mensaje recibido desde: ${message.from}")
        Log.d(TAG, "Notification title: ${message.notification?.title}")
        Log.d(TAG, "Notification body: ${message.notification?.body}")
        Log.d(TAG, "Data: ${message.data}")

        val operation = message.data["operation"]
            ?: message.data["operacion"]
            ?: message.data["tipo"]
            ?: "info"

        val title = message.data["title"]
            ?: message.data["titulo"]
            ?: message.data["name"]
            ?: message.data["nombre"]
            ?: message.notification?.title
            ?: "Sin título"

        val director = message.data["director"] ?: "Desconocido"
        val year = message.data["year"]?.toIntOrNull() ?: 2024

        val notificationBody = when (operation.lowercase()) {
            "alta", "add", "create" -> {
                val wasAdded = FilmDataSource.addOrUpdateFilm(
                    title = title,
                    director = director,
                    year = year,
                    genre = Film.GENRE_SCIFI,
                    format = Film.FORMAT_ONLINE,
                    imdbUrl = message.data["imdbUrl"] ?: "",
                    comments = "Alta recibida por Firebase Cloud Messaging"
                )

                if (wasAdded) {
                    Log.d(TAG, "Alta de película: $title")
                    "alta: $title"
                } else {
                    Log.d(TAG, "Actualización de película: $title")
                    "actualizada: $title"
                }
            }

            "baja", "delete", "remove" -> {
                val removed = FilmDataSource.deleteFilmByTitle(title)

                if (removed) {
                    Log.d(TAG, "Baja de película: $title")
                    "baja: $title"
                } else {
                    Log.d(TAG, "Baja ignorada. No existe: $title")
                    "baja ignorada: $title"
                }
            }

            else -> {
                message.notification?.body ?: "info: $title"
            }
        }

        showNotification(
            title = "Filmoteca",
            message = notificationBody,
            operation = operation,
            movieTitle = title
        )
    }

    private fun showNotification(
        title: String,
        message: String,
        operation: String,
        movieTitle: String
    ) {
        val intent = Intent(this, FilmListActivity::class.java).apply {
            putExtra("operation", operation)
            putExtra("title", movieTitle)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
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