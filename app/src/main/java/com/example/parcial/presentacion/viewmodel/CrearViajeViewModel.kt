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
        val viajeCreado: Viaje? = null,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(CrearViajeUiState())
    val uiState: StateFlow<CrearViajeUiState> = _uiState.asStateFlow()

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, viajeCreado = null, error = null) }
    }

    fun onDestinoChange(value: String) {
        _uiState.update { it.copy(destino = value, viajeCreado = null, error = null) }
    }

    fun onFechaInicioChange(value: LocalDate) {
        _uiState.update { it.copy(fechaInicio = value, viajeCreado = null, error = null) }
    }

    fun onFechaFinChange(value: LocalDate) {
        _uiState.update { it.copy(fechaFin = value, viajeCreado = null, error = null) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun crearViaje() {
        val state = _uiState.value

        viewModelScope.launch {
            crearViajeUseCase(
                nombre = state.nombre.trim(),
                destino = state.destino.trim(),
                fecha_inicio = state.fechaInicio,
                fecha_fin = state.fechaFin
            ).onSuccess { viaje ->
                _uiState.update { it.copy(viajeCreado = viaje, error = null) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message ?: "No se pudo crear el viaje") }
            }
        }
    }

    fun reset() {
        _uiState.value = CrearViajeUiState()
    }

    companion object {
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
