package com.example.parcial.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preparcial.domain.model.Lugar
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.usecase.GetLugaresUseCase
import com.example.preparcial.domain.usecase.GetViajeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleViajeViewModel(
    private val getViajeUseCase: GetViajeUseCase,
    private val getLugaresUseCase: GetLugaresUseCase
) : ViewModel() {

    data class DetalleViajeUiState(
        val cargando: Boolean = false,
        val viaje: Viaje? = null,
        val lugares: List<Lugar> = emptyList(),
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(DetalleViajeUiState())
    val uiState: StateFlow<DetalleViajeUiState> = _uiState.asStateFlow()

    fun cargar(tripId: String) {
        if (tripId.isBlank()) {
            _uiState.update { it.copy(error = "No se identificó el viaje") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            val viajeResultado = getViajeUseCase(tripId)
            val lugaresResultado = getLugaresUseCase(tripId)

            if (viajeResultado.isFailure) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        viaje = null,
                        lugares = emptyList(),
                        error = viajeResultado.exceptionOrNull()?.message ?: "No se pudo cargar el viaje"
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    cargando = false,
                    viaje = viajeResultado.getOrThrow(),
                    lugares = lugaresResultado.getOrElse { emptyList() },
                    error = if (lugaresResultado.isFailure) {
                        lugaresResultado.exceptionOrNull()?.message ?: "No se pudieron cargar los lugares"
                    } else {
                        null
                    }
                )
            }
        }
    }
}
