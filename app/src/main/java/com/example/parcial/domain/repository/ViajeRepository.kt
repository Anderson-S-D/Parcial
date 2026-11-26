package com.example.preparcial.domain.repository

import com.example.preparcial.domain.model.Categoria
import com.example.preparcial.domain.model.Lugar
import com.example.preparcial.domain.model.Viaje
import java.time.LocalDate

interface ViajeRepository {
    suspend fun getViajes(): Result<List<Viaje>>
    suspend fun getViaje(tripId: String): Result<Viaje>
    suspend fun getLugares(tripId: String): Result<List<Lugar>>
    suspend fun getCategorias(): Result<List<Categoria>>
    suspend fun crearViaje(
        nombre: String,
        destino: String,
        fecha_inicio: LocalDate,
        fecha_fin: LocalDate
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
