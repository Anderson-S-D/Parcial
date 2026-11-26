package com.example.parcial.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.domain.model.Viaje
import com.example.parcial.domain.usecase.CrearViajeUseCase
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
        val guardando: Boolean = false,
        val viajeCreado: Viaje? = null,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(CrearViajeUiState())
    val uiState: StateFlow<CrearViajeUiState> = _uiState.asStateFlow()

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, error = null, viajeCreado = null) }
    }

    fun onDestinoChange(value: String) {
        _uiState.update { it.copy(destino = value, error = null, viajeCreado = null) }
    }

    fun onFechaInicioChange(value: LocalDate) {
        _uiState.update { it.copy(fechaInicio = value, error = null, viajeCreado = null) }
    }

    fun onFechaFinChange(value: LocalDate) {
        _uiState.update { it.copy(fechaFin = value, error = null, viajeCreado = null) }
    }

    fun crearViaje() {
        if (_uiState.value.guardando) return
        val estado = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, error = null) }
            crearViajeUseCase(
                nombre = estado.nombre,
                destino = estado.destino,
                fechaInicio = estado.fechaInicio,
                fechaFin = estado.fechaFin
            ).fold(
                onSuccess = { viaje ->
                    _uiState.update {
                        it.copy(guardando = false, viajeCreado = viaje, error = null)
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(guardando = false, error = e.message ?: "No se pudo crear el viaje")
                    }
                }
            )
        }
    }

    fun reset() {
        _uiState.value = CrearViajeUiState()
    }
}
