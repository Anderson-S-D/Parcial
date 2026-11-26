package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Lugar
import com.example.preparcial.domain.repository.ViajeRepository

class GetLugaresUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(tripId: String): Result<List<Lugar>> {
        if (tripId.isBlank()) {
            return Result.failure(IllegalArgumentException("El id del viaje es requerido"))
        }
        return repository.getLugares(tripId).map { lugares ->
            // Doble filtro: aunque la API traiga más datos, solo mostramos los de este viaje.
            lugares.filter { it.tripId == tripId }
        }
    }
}
