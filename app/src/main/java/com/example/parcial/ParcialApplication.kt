package com.example.parcial

import android.app.Application
import com.example.preparcial.di.AppContainer

/**
 * Clase Application principal.
 * Inicializa el contenedor de inyección de dependencias manual (AppContainer).
 */
class ParcialApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
