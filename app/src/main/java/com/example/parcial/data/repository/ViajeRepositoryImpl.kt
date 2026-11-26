package com.example.parcial.data.repository

import com.example.parcial.data.remote.ApiService
import com.example.parcial.data.remote.CrearLugarRequestDto
import com.example.parcial.data.remote.CrearViajeRequestDto
import com.example.preparcial.domain.model.Categoria
import com.example.preparcial.domain.model.Lugar
import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.repository.ViajeRepository
import retrofit2.Response
import java.time.LocalDate

class ViajeRepositoryImpl(
    private val api: ApiService
) : ViajeRepository {

    override suspend fun getViajes(): Result<List<Viaje>> {
        return ejecutar { api.getViajes() }.map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun getViaje(tripId: String): Result<Viaje> {
        return ejecutar { api.getViajeById(tripId) }.map { it.toDomain() }
    }

    override suspend fun getLugares(tripId: String): Result<List<Lugar>> {
        return ejecutar { api.getLugares(tripId) }.map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun getCategorias(): Result<List<Categoria>> {
        return ejecutar { api.getCategorias() }.map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun crearViaje(
        nombre: String,
        destino: String,
        fecha_inicio: LocalDate,
        fecha_fin: LocalDate
    ): Result<Viaje> {
        val request = CrearViajeRequestDto(
            nombre = nombre,
            destino = destino,
            fechaInicio = fecha_inicio.toString(),
            fechaFin = fecha_fin.toString()
        )
        return ejecutar { api.crearViaje(request) }.map { it.toDomain() }
    }

    override suspend fun crearLugar(
        tripId: String,
        nombre: String,
        descripcion: String,
        ciudad: String,
        categoriaId: Int,
        fechaTentativa: LocalDate
    ): Result<Lugar> {
        val request = CrearLugarRequestDto(
            viajeId = tripId,
            nombre = nombre,
            descripcion = descripcion,
            ciudad = ciudad,
            categoriaId = categoriaId,
            fechaTentativa = fechaTentativa.toString()
        )
        return ejecutar { api.crearLugar(request) }.map { it.toDomain() }
    }

    private suspend fun <T> ejecutar(llamada: suspend () -> Response<T>): Result<T> {
        return try {
            val response = llamada()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(IllegalStateException("El servidor devolvió una respuesta vacía"))
                }
            } else {
                val detalle = runCatching { response.errorBody()?.string() }.getOrNull()
                Result.failure(
                    IllegalStateException(detalle ?: "Error ${response.code()} al consultar el servidor")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
