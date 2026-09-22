package pe.edu.upeu.andinasalud

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // initKoin() se reescribe en el ítem de Inyección de Dependencias
    }
}