package es.ua.eps.filmoteca

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashLogger.install(this)
    }
}
