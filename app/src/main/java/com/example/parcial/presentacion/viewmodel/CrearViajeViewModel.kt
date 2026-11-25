package com.example.parcial.presentacion.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.usecase.CrearViajeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class CrearViajeViewModel(
    private val crearViajeUseCase: CrearViajeUseCase
) : ViewModel() {

    data class CrearViajeUiState(
        val nombre: String = "",
        val destino: String = "",
        val fechaInicio: LocalDate? = null,
        val fechaFin: LocalDate? = null,
        val viajeCreado: Viaje? = null
    )

    private val _uiState = MutableStateFlow(CrearViajeUiState())
    val uiState: StateFlow<CrearViajeUiState> = _uiState.asStateFlow()

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, viajeCreado = null) }
    }

    fun onDestinoChange(value: String) {
        _uiState.update { it.copy(destino = value,viajeCreado = null) }
    }

    fun onFechaInicioChange(value: LocalDate) {
        _uiState.update { it.copy(fechaInicio = value,viajeCreado = null) }
    }

    fun onFechaFinChange(value: LocalDate) {
        _uiState.update { it.copy(fechaFin = value,viajeCreado = null) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun crearViaje() {
        val state = _uiState.value

        val nombre = state.nombre.trim()

        val destino = state.destino.trim()

        val fin = state.fechaFin ?: run {
            _uiState.update { it.copy(error = "Seleccione la fecha de fin") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy() }

            crearViajeUseCase(
                nombre = nombre,
                destino = destino,
                fecha_inicio = inicio,
                fecha_fin = fin
            )
                    }
                }
            )
        }
    }


    fun reset() {
        _uiState.value = CrearViajeUiState()
    }

    object {
        fun factory(crearViajeUseCase: CrearViajeUseCase) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CrearViajeViewModel::class.java)) {
                        return CrearViajeViewModel(crearViajeUseCase) as T
                    }
                    throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
                }
            }
    }
}