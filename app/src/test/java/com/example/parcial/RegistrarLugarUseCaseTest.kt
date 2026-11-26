package com.example.parcial

import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.usecase.GetLugaresUseCase
import com.example.preparcial.domain.usecase.RegistrarLugarUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class RegistrarLugarUseCaseTest {

    private val inicio = LocalDate.of(2026, 6, 10)
    private val fin = LocalDate.of(2026, 6, 20)

    private fun repositoryConViaje(): FakeViajeRepository {
        val repository = FakeViajeRepository()
        repository.viajes.add(Viaje("v1", "Europa 2026", "Italia", inicio, fin))
        return repository
    }

    @Test
    fun `registrar lugar valido guarda el lugar del viaje correcto`() = runTest {
        val repository = repositoryConViaje()
        val useCase = RegistrarLugarUseCase(repository)

        val resultado = useCase(
            tripId = "v1",
            nombre = "Coliseo Romano",
            descripcion = "Anfiteatro romano",
            ciudad = "Roma",
            categoriaId = 1,
            fechaTentativa = LocalDate.of(2026, 6, 12)
        )

        assertTrue(resultado.isSuccess)
        assertEquals(1, repository.crearLugarLlamadas)
        assertEquals("v1", repository.lugares.first().tripId)
        assertEquals(1, repository.lugares.first().categoriaId)
    }

    @Test
    fun `fecha tentativa dentro del rango incluye los limites`() = runTest {
        val repository = repositoryConViaje()
        val useCase = RegistrarLugarUseCase(repository)

        val enInicio = useCase("v1", "A", "Desc", "Roma", 1, inicio)
        val enFin = useCase("v1", "B", "Desc", "Roma", 1, fin)

        assertTrue(enInicio.isSuccess)
        assertTrue(enFin.isSuccess)
        assertEquals(2, repository.crearLugarLlamadas)
    }

    @Test
    fun `fecha tentativa antes o despues del viaje no guarda`() = runTest {
        val repository = repositoryConViaje()
        val useCase = RegistrarLugarUseCase(repository)

        val antes = useCase("v1", "A", "Desc", "Roma", 1, inicio.minusDays(1))
        val despues = useCase("v1", "B", "Desc", "Roma", 1, fin.plusDays(1))

        assertTrue(antes.isFailure)
        assertTrue(despues.isFailure)
        assertEquals(0, repository.crearLugarLlamadas)
    }

    @Test
    fun `campos obligatorios vacios no guardan`() = runTest {
        val repository = repositoryConViaje()
        val useCase = RegistrarLugarUseCase(repository)

        assertTrue(useCase("v1", "", "Desc", "Roma", 1, inicio).isFailure)
        assertTrue(useCase("v1", "A", "", "Roma", 1, inicio).isFailure)
        assertTrue(useCase("v1", "A", "Desc", "", 1, inicio).isFailure)
        assertTrue(useCase("v1", "A", "Desc", "Roma", null, inicio).isFailure)
        assertTrue(useCase("v1", "A", "Desc", "Roma", 1, null).isFailure)
        assertEquals(0, repository.crearLugarLlamadas)
    }

    @Test
    fun `getLugares filtra por tripId`() = runTest {
        val repository = repositoryConViaje()
        repository.viajes.add(Viaje("v2", "Otro viaje", "Francia", inicio, fin))
        repository.lugares.add(
            com.example.preparcial.domain.model.Lugar(
                id = "l1", nombre = "Coliseo", descripcion = "Desc", ciudad = "Roma",
                fecha_tentativa = inicio, tripId = "v1"
            )
        )
        repository.lugares.add(
            com.example.preparcial.domain.model.Lugar(
                id = "l2", nombre = "Louvre", descripcion = "Desc", ciudad = "París",
                fecha_tentativa = inicio, tripId = "v2"
            )
        )
        val useCase = GetLugaresUseCase(repository)

        val resultado = useCase("v1")

        assertTrue(resultado.isSuccess)
        assertEquals(1, resultado.getOrThrow().size)
        assertEquals("l1", resultado.getOrThrow().first().id)
    }
}
