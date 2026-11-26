package com.example.parcial.data.repository

import com.example.parcial.data.remote.ApiService
import com.example.parcial.data.remote.CrearLugarRequestDto
import com.example.parcial.data.remote.CrearViajeRequestDto
import com.example.parcial.domain.model.Categoria
import com.example.parcial.domain.model.Lugar
import com.example.parcial.domain.model.Viaje
import com.example.parcial.domain.repository.ViajeRepository
import com.google.gson.JsonParser
import kotlinx.coroutines.CancellationException
import retrofit2.Response
import java.io.IOException
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
        fechaInicio: LocalDate,
        fechaFin: LocalDate
    ): Result<Viaje> {
        val request = CrearViajeRequestDto(
            nombre = nombre,
            destino = destino,
            fechaInicio = fechaInicio.toString(),
            fechaFin = fechaFin.toString()
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
            val body = response.body()
            when {
                response.isSuccessful && body != null -> Result.success(body)
                response.isSuccessful -> Result.failure(IllegalStateException(MENSAJE_RESPUESTA_VACIA))
                else -> Result.failure(IllegalStateException(mensajeDeError(response)))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(IOException(MENSAJE_SIN_CONEXION, e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mensajeDeError(response: Response<*>): String {
        val cuerpo = runCatching { response.errorBody()?.string() }.getOrNull()
        val detalle = cuerpo?.let {
            runCatching { JsonParser.parseString(it).asJsonObject.get("detail") }.getOrNull()
        }
        return when {
            detalle != null && detalle.isJsonPrimitive -> detalle.asString
            detalle != null && detalle.isJsonArray -> MENSAJE_DATOS_INVALIDOS
            else -> "Error ${response.code()} al consultar el servidor"
        }
    }

    companion object {
        const val MENSAJE_SIN_CONEXION = "No se pudo conectar con el servidor"
        const val MENSAJE_RESPUESTA_VACIA = "El servidor devolvió una respuesta vacía"
        const val MENSAJE_DATOS_INVALIDOS = "Los datos enviados no son válidos"
    }
}
