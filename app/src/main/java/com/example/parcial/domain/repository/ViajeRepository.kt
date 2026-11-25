package com.example.preparcial.domain.repository

import com.example.preparcial.domain.model.Categoria
import com.example.preparcial.domain.model.Viaje
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ViajeRepository {
    fun getViajes(): Flow<List<Viaje>>
    fun getViajesById(id: String): Flow<Viaje?>
    suspend fun crearViaje(nombre: String, destino: String, fecha_inicio: LocalDate, fecha_fin: LocalDate): Result<Viaje>
    suspend fun registrarLugar(nombre: String, descripcion: String, ciudad: String, categoria: List<Categoria>, fecha_tentativa:LocalDate): Result<Unit>
}
