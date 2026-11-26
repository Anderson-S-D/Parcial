package com.example.parcial.domain.repository

import com.example.parcial.domain.model.Categoria
import com.example.parcial.domain.model.Lugar
import com.example.parcial.domain.model.Viaje
import java.time.LocalDate

interface ViajeRepository {
    suspend fun getViajes(): Result<List<Viaje>>
    suspend fun getViaje(tripId: String): Result<Viaje>
    suspend fun getLugares(tripId: String): Result<List<Lugar>>
    suspend fun getCategorias(): Result<List<Categoria>>
    suspend fun crearViaje(
        nombre: String,
        destino: String,
        fechaInicio: LocalDate,
        fechaFin: LocalDate
    ): Result<Viaje>
    suspend fun crearLugar(
        tripId: String,
        nombre: String,
        descripcion: String,
        ciudad: String,
        categoriaId: Int,
        fechaTentativa: LocalDate
    ): Result<Lugar>
}
