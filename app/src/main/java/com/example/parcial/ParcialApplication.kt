package com.example.parcial

import android.app.Application
import com.example.parcial.di.AppContainer

class ParcialApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
