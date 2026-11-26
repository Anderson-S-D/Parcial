package com.example.parcial

import com.example.parcial.presentacion.viewmodel.DetalleViajeViewModel
import com.example.preparcial.domain.model.Lugar
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.usecase.GetLugaresUseCase
import com.example.preparcial.domain.usecase.GetViajeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class DetalleViajeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fecha = LocalDate.of(2026, 6, 12)

    private fun repositoryConDatos(): FakeViajeRepository {
        val repository = FakeViajeRepository()
        repository.viajes.add(Viaje("v1", "Europa 2026", "Italia", LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 20)))
        repository.viajes.add(Viaje("v2", "Otro viaje", "Francia", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5)))
        repository.lugares.add(Lugar("l1", "Coliseo", "Desc", "Roma", fecha_tentativa = fecha, tripId = "v1"))
        repository.lugares.add(Lugar("l2", "Louvre", "Desc", "París", fecha_tentativa = fecha, tripId = "v2"))
        return repository
    }

    @Test
    fun `cargar detalle muestra solo los lugares del viaje`() = runTest {
        val repository = repositoryConDatos()
        val viewModel = DetalleViajeViewModel(GetViajeUseCase(repository), GetLugaresUseCase(repository))

        viewModel.cargar("v1")

        val estado = viewModel.uiState.value
        assertEquals(false, estado.cargando)
        assertEquals("Europa 2026", estado.viaje?.nombre)
        assertEquals(1, estado.lugares.size)
        assertEquals("l1", estado.lugares.first().id)
        assertEquals(null, estado.error)
    }

    @Test
    fun `cargar detalle de viaje inexistente muestra error`() = runTest {
        val repository = repositoryConDatos()
        val viewModel = DetalleViajeViewModel(GetViajeUseCase(repository), GetLugaresUseCase(repository))

        viewModel.cargar("v999")

        assertEquals(null, viewModel.uiState.value.viaje)
        assertTrue(viewModel.uiState.value.lugares.isEmpty())
        assertTrue(viewModel.uiState.value.error!!.isNotBlank())
    }
}
