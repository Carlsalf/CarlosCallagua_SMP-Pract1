package es.ua.eps.filmoteca

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CrashLogger {
    fun install(appContext: Context) {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            val stack = sw.toString()

            // Logcat
            Log.e("CRASH", "Uncaught in thread ${t.name}\n$stack")

            // Guarda en archivo
            val time = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(Date())
            val file = File(appContext.filesDir, "crash_$time.txt")
            file.writeText(stack)

            // Diálogo visible (en main thread si se puede)
            try {
                val builder = AlertDialog.Builder(appContext, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                    .setTitle("Se produjo un error")
                    .setMessage(stack.lineSequence().take(12).joinToString("\n"))
                    .setPositiveButton("Cerrar app") { _, _ -> kotlin.system.exitProcess(2) }
                builder.create().apply {
                    window?.setType(android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                }
            } catch (_: Throwable) {
                // Si no podemos mostrar diálogo, cerramos
            }

            // Cierra la app limpiamente
            kotlin.system.exitProcess(2)
        }
    }
}
