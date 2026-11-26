package com.example.parcial

import com.example.preparcial.domain.usecase.CrearViajeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class CrearViajeUseCaseTest {

    private val inicio = LocalDate.of(2026, 6, 10)
    private val fin = LocalDate.of(2026, 6, 20)

    @Test
    fun `crear viaje valido guarda el registro`() = runTest {
        val repository = FakeViajeRepository()
        val useCase = CrearViajeUseCase(repository)

        val resultado = useCase("Europa 2026", "Italia", inicio, fin)

        assertTrue(resultado.isSuccess)
        assertEquals(1, repository.crearViajeLlamadas)
        assertEquals("Europa 2026", repository.viajes.first().nombre)
        assertEquals("Italia", repository.viajes.first().destino)
    }

    @Test
    fun `crear viaje con nombre o destino vacio no guarda`() = runTest {
        val repository = FakeViajeRepository()
        val useCase = CrearViajeUseCase(repository)

        val sinNombre = useCase("", "Italia", inicio, fin)
        val sinDestino = useCase("Europa 2026", "  ", inicio, fin)

        assertTrue(sinNombre.isFailure)
        assertTrue(sinDestino.isFailure)
        assertEquals(0, repository.crearViajeLlamadas)
    }

    @Test
    fun `crear viaje con fecha fin anterior al inicio no guarda`() = runTest {
        val repository = FakeViajeRepository()
        val useCase = CrearViajeUseCase(repository)

        val resultado = useCase("Europa 2026", "Italia", fin, inicio)

        assertTrue(resultado.isFailure)
        assertEquals(0, repository.crearViajeLlamadas)
    }

    @Test
    fun `crear viaje con fechas iguales es valido`() = runTest {
        val repository = FakeViajeRepository()
        val useCase = CrearViajeUseCase(repository)

        val resultado = useCase("Viaje express", "Medellín", inicio, inicio)

        assertTrue(resultado.isSuccess)
        assertEquals(1, repository.crearViajeLlamadas)
    }
}
