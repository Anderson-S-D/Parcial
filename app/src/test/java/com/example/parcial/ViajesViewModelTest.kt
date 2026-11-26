package com.example.parcial

import com.example.parcial.presentacion.viewmodel.ViajesViewModel
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.usecase.GetViajesUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class ViajesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `carga viajes desde el repositorio`() = runTest {
        val repository = FakeViajeRepository()
        repository.viajes.add(Viaje("v1", "Europa 2026", "Italia", LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20)))

        val viewModel = ViajesViewModel(GetViajesUseCase(repository))

        val estado = viewModel.uiState.value
        assertEquals(false, estado.cargando)
        assertEquals(1, estado.viajes.size)
        assertEquals("Europa 2026", estado.viajes.first().nombre)
        assertEquals("Italia", estado.viajes.first().destino)
        assertEquals(null, estado.error)
    }

    @Test
    fun `error al cargar viajes se muestra en el estado`() = runTest {
        val repository = FakeViajeRepository()
        repository.errorAlCargar = RuntimeException("Sin conexión")

        val viewModel = ViajesViewModel(GetViajesUseCase(repository))

        val estado = viewModel.uiState.value
        assertEquals(false, estado.cargando)
        assertTrue(estado.viajes.isEmpty())
        assertTrue(estado.error!!.contains("Sin conexión"))
    }
}
