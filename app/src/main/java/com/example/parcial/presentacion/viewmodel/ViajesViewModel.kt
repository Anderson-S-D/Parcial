package com.example.parcial.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.usecase.GetViajesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViajesViewModel(
    private val getViajesUseCase: GetViajesUseCase
) : ViewModel() {

    data class ViajesUiState(
        val cargando: Boolean = false,
        val viajes: List<Viaje> = emptyList(),
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(ViajesUiState())
    val uiState: StateFlow<ViajesUiState> = _uiState.asStateFlow()

    init {
        cargarViajes()
    }

    fun cargarViajes() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            getViajesUseCase().fold(
                onSuccess = { viajes ->
                    _uiState.update { it.copy(cargando = false, viajes = viajes, error = null) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(cargando = false, error = e.message ?: "No se pudieron cargar los viajes")
                    }
                }
            )
        }
    }
}
