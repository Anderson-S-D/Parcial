package com.example.parcial.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parcial.domain.usecase.CrearViajeUseCase
import com.example.parcial.domain.usecase.GetCategoriasUseCase
import com.example.parcial.domain.usecase.GetLugaresUseCase
import com.example.parcial.domain.usecase.GetViajeUseCase
import com.example.parcial.domain.usecase.GetViajesUseCase
import com.example.parcial.domain.usecase.RegistrarLugarUseCase

class ViewModelFactory(
    private val getViajesUseCase: GetViajesUseCase,
    private val crearViajeUseCase: CrearViajeUseCase,
    private val getViajeUseCase: GetViajeUseCase,
    private val getLugaresUseCase: GetLugaresUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val registrarLugarUseCase: RegistrarLugarUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViajesViewModel::class.java) ->
                ViajesViewModel(getViajesUseCase) as T
            modelClass.isAssignableFrom(CrearViajeViewModel::class.java) ->
                CrearViajeViewModel(crearViajeUseCase) as T
            modelClass.isAssignableFrom(DetalleViajeViewModel::class.java) ->
                DetalleViajeViewModel(getViajeUseCase, getLugaresUseCase) as T
            modelClass.isAssignableFrom(RegistrarLugarViewModel::class.java) ->
                RegistrarLugarViewModel(registrarLugarUseCase, getCategoriasUseCase) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
