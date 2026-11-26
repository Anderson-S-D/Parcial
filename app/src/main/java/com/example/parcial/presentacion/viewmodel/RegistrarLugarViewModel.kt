package com.example.parcial.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.domain.model.Categoria
import com.example.parcial.domain.usecase.GetCategoriasUseCase
import com.example.parcial.domain.usecase.RegistrarLugarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class RegistrarLugarViewModel(
    private val registrarLugarUseCase: RegistrarLugarUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase
) : ViewModel() {

    data class RegistrarLugarUiState(
        val nombre: String = "",
        val descripcion: String = "",
        val ciudad: String = "",
        val categoriaId: Int? = null,
        val fechaTentativa: LocalDate? = null,
        val categorias: List<Categoria> = emptyList(),
        val cargandoCategorias: Boolean = false,
        val guardando: Boolean = false,
        val guardado: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(RegistrarLugarUiState())
    val uiState: StateFlow<RegistrarLugarUiState> = _uiState.asStateFlow()

    private var tripId: String? = null

    init {
        cargarCategorias()
    }

    fun preparar(viajeId: String) {
        tripId = viajeId
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargandoCategorias = true, error = null) }
            getCategoriasUseCase().fold(
                onSuccess = { categorias ->
                    _uiState.update { it.copy(cargandoCategorias = false, categorias = categorias) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            cargandoCategorias = false,
                            error = e.message ?: "No se pudieron cargar las categorías"
                        )
                    }
                }
            )
        }
    }

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, error = null) }
    }

    fun onDescripcionChange(value: String) {
        _uiState.update { it.copy(descripcion = value, error = null) }
    }

    fun onCiudadChange(value: String) {
        _uiState.update { it.copy(ciudad = value, error = null) }
    }

    fun onCategoriaChange(value: Int) {
        _uiState.update { it.copy(categoriaId = value, error = null) }
    }

    fun onFechaTentativaChange(value: LocalDate) {
        _uiState.update { it.copy(fechaTentativa = value, error = null) }
    }

    fun guardar() {
        if (_uiState.value.guardando) return
        val viajeId = tripId
        if (viajeId.isNullOrBlank()) {
            _uiState.update { it.copy(error = "No se identificó el viaje") }
            return
        }
        val estado = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, error = null) }
            registrarLugarUseCase(
                tripId = viajeId,
                nombre = estado.nombre,
                descripcion = estado.descripcion,
                ciudad = estado.ciudad,
                categoriaId = estado.categoriaId,
                fechaTentativa = estado.fechaTentativa
            ).fold(
                onSuccess = {
                    _uiState.update { it.copy(guardando = false, guardado = true, error = null) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(guardando = false, error = e.message ?: "No se pudo registrar el lugar")
                    }
                }
            )
        }
    }

    fun reset() {
        _uiState.value = RegistrarLugarUiState(categorias = _uiState.value.categorias)
    }
}
