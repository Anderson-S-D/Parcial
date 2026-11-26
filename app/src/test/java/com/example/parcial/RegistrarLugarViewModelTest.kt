package com.example.parcial

import com.example.parcial.presentacion.viewmodel.RegistrarLugarViewModel
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.usecase.GetCategoriasUseCase
import com.example.preparcial.domain.usecase.RegistrarLugarUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class RegistrarLugarViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val inicio = LocalDate.of(2026, 6, 10)
    private val fin = LocalDate.of(2026, 6, 20)

    private fun viewModelConViaje(): Pair<RegistrarLugarViewModel, FakeViajeRepository> {
        val repository = FakeViajeRepository()
        repository.viajes.add(Viaje("v1", "Europa 2026", "Italia", inicio, fin))
        val viewModel = RegistrarLugarViewModel(
            RegistrarLugarUseCase(repository),
            GetCategoriasUseCase(repository)
        )
        viewModel.preparar("v1")
        return viewModel to repository
    }

    @Test
    fun `carga las categorias al iniciar`() = runTest {
        val (viewModel, _) = viewModelConViaje()

        assertEquals(1, viewModel.uiState.value.categorias.size)
        assertEquals("Histórico", viewModel.uiState.value.categorias.first().nombre)
    }

    @Test
    fun `guardar lugar valido marca guardado y asocia el viaje`() = runTest {
        val (viewModel, repository) = viewModelConViaje()

        viewModel.onNombreChange("Coliseo Romano")
        viewModel.onDescripcionChange("Anfiteatro romano")
        viewModel.onCiudadChange("Roma")
        viewModel.onCategoriaChange(1)
        viewModel.onFechaTentativaChange(LocalDate.of(2026, 6, 12))
        viewModel.guardar()

        val estado = viewModel.uiState.value
        assertTrue(estado.guardado)
        assertEquals(null, estado.error)
        assertEquals(1, repository.crearLugarLlamadas)
        assertEquals("v1", repository.lugares.first().tripId)
    }

    @Test
    fun `guardar lugar con fecha fuera del viaje no persiste`() = runTest {
        val (viewModel, repository) = viewModelConViaje()

        viewModel.onNombreChange("Coliseo Romano")
        viewModel.onDescripcionChange("Anfiteatro romano")
        viewModel.onCiudadChange("Roma")
        viewModel.onCategoriaChange(1)
        viewModel.onFechaTentativaChange(fin.plusDays(1))
        viewModel.guardar()

        assertTrue(viewModel.uiState.value.error!!.isNotBlank())
        assertEquals(false, viewModel.uiState.value.guardado)
        assertEquals(0, repository.crearLugarLlamadas)
    }

    @Test
    fun `guardar sin campos obligatorios no persiste`() = runTest {
        val (viewModel, repository) = viewModelConViaje()

        viewModel.guardar()

        assertTrue(viewModel.uiState.value.error!!.isNotBlank())
        assertEquals(0, repository.crearLugarLlamadas)
    }
}
