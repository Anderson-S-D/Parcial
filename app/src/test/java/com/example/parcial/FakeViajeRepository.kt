package com.example.parcial

import com.example.preparcial.domain.model.Categoria
import com.example.preparcial.domain.model.Lugar
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.repository.ViajeRepository
import java.time.LocalDate

class FakeViajeRepository : ViajeRepository {

    val viajes = mutableListOf<Viaje>()
    val lugares = mutableListOf<Lugar>()
    var categorias: List<Categoria> = listOf(Categoria("1", "Histórico"))
    var errorAlCargar: Throwable? = null
    var crearViajeLlamadas = 0
    var crearLugarLlamadas = 0

    override suspend fun getViajes(): Result<List<Viaje>> {
        val error = errorAlCargar
        return if (error != null) Result.failure(error) else Result.success(viajes.toList())
    }

    override suspend fun getViaje(tripId: String): Result<Viaje> {
        val viaje = viajes.firstOrNull { it.id == tripId }
        return if (viaje != null) {
            Result.success(viaje)
        } else {
            Result.failure(NoSuchElementException("Viaje no encontrado"))
        }
    }

    override suspend fun getLugares(tripId: String): Result<List<Lugar>> {
        return Result.success(lugares.filter { it.tripId == tripId })
    }

    override suspend fun getCategorias(): Result<List<Categoria>> = Result.success(categorias)

    override suspend fun crearViaje(
        nombre: String,
        destino: String,
        fecha_inicio: LocalDate,
        fecha_fin: LocalDate
    ): Result<Viaje> {
        crearViajeLlamadas++
        val viaje = Viaje(
            id = "v-${viajes.size + 1}",
            nombre = nombre,
            destino = destino,
            fecha_inicio = fecha_inicio,
            fecha_fin = fecha_fin
        )
        viajes.add(viaje)
        return Result.success(viaje)
    }

    override suspend fun crearLugar(
        tripId: String,
        nombre: String,
        descripcion: String,
        ciudad: String,
        categoriaId: Int,
        fechaTentativa: LocalDate
    ): Result<Lugar> {
        crearLugarLlamadas++
        val lugar = Lugar(
            id = "l-${lugares.size + 1}",
            nombre = nombre,
            descripcion = descripcion,
            ciudad = ciudad,
            categoria = listOfNotNull(categorias.firstOrNull { it.id == categoriaId.toString() }),
            fecha_tentativa = fechaTentativa,
            tripId = tripId,
            categoriaId = categoriaId
        )
        lugares.add(lugar)
        return Result.success(lugar)
    }
}
