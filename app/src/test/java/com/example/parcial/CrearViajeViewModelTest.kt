package com.example.parcial

import com.example.parcial.presentacion.viewmodel.CrearViajeViewModel
import com.example.preparcial.domain.usecase.CrearViajeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class CrearViajeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val inicio = LocalDate.of(2026, 6, 10)
    private val fin = LocalDate.of(2026, 6, 20)

    @Test
    fun `guardar viaje valido actualiza el estado`() = runTest {
        val repository = FakeViajeRepository()
        val viewModel = CrearViajeViewModel(CrearViajeUseCase(repository))

        viewModel.onNombreChange("Europa 2026")
        viewModel.onDestinoChange("Italia")
        viewModel.onFechaInicioChange(inicio)
        viewModel.onFechaFinChange(fin)
        viewModel.crearViaje()

        val estado = viewModel.uiState.value
        assertEquals(false, estado.guardando)
        assertNotNull(estado.viajeCreado)
        assertEquals(null, estado.error)
        assertEquals(1, repository.crearViajeLlamadas)
    }

    @Test
    fun `guardar viaje con campos vacios muestra error y no guarda`() = runTest {
        val repository = FakeViajeRepository()
        val viewModel = CrearViajeViewModel(CrearViajeUseCase(repository))

        viewModel.onFechaInicioChange(inicio)
        viewModel.onFechaFinChange(fin)
        viewModel.crearViaje()

        assertTrue(viewModel.uiState.value.error!!.isNotBlank())
        assertEquals(null, viewModel.uiState.value.viajeCreado)
        assertEquals(0, repository.crearViajeLlamadas)
    }

    @Test
    fun `guardar viaje con fecha fin anterior muestra error y no guarda`() = runTest {
        val repository = FakeViajeRepository()
        val viewModel = CrearViajeViewModel(CrearViajeUseCase(repository))

        viewModel.onNombreChange("Europa 2026")
        viewModel.onDestinoChange("Italia")
        viewModel.onFechaInicioChange(fin)
        viewModel.onFechaFinChange(inicio)
        viewModel.crearViaje()

        assertTrue(viewModel.uiState.value.error!!.contains("fecha fin"))
        assertEquals(0, repository.crearViajeLlamadas)
    }
}
