package com.example.parcial.di

import androidx.lifecycle.ViewModelProvider
import com.example.parcial.BuildConfig
import com.example.parcial.data.remote.RetrofitClient
import com.example.parcial.data.repository.ViajeRepositoryImpl
import com.example.parcial.presentacion.viewmodel.ViewModelFactory
import com.example.preparcial.domain.repository.ViajeRepository
import com.example.preparcial.domain.usecase.CrearViajeUseCase
import com.example.preparcial.domain.usecase.GetCategoriasUseCase
import com.example.preparcial.domain.usecase.GetLugaresUseCase
import com.example.preparcial.domain.usecase.GetViajeUseCase
import com.example.preparcial.domain.usecase.GetViajesUseCase
import com.example.preparcial.domain.usecase.RegistrarLugarUseCase

class AppContainer {

    private val apiService by lazy { RetrofitClient.crear(BuildConfig.API_BASE_URL) }

    private val repository: ViajeRepository by lazy { ViajeRepositoryImpl(apiService) }

    private val getViajesUseCase by lazy { GetViajesUseCase(repository) }
    private val crearViajeUseCase by lazy { CrearViajeUseCase(repository) }
    private val getViajeUseCase by lazy { GetViajeUseCase(repository) }
    private val getLugaresUseCase by lazy { GetLugaresUseCase(repository) }
    private val getCategoriasUseCase by lazy { GetCategoriasUseCase(repository) }
    private val registrarLugarUseCase by lazy { RegistrarLugarUseCase(repository) }

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(
            getViajesUseCase = getViajesUseCase,
            crearViajeUseCase = crearViajeUseCase,
            getViajeUseCase = getViajeUseCase,
            getLugaresUseCase = getLugaresUseCase,
            getCategoriasUseCase = getCategoriasUseCase,
            registrarLugarUseCase = registrarLugarUseCase
        )
    }
}
