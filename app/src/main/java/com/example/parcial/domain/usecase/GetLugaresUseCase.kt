package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.Lugar
import com.example.parcial.domain.repository.ViajeRepository

class GetLugaresUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(tripId: String): Result<List<Lugar>> {
        if (tripId.isBlank()) {
            return Result.failure(IllegalArgumentException("El id del viaje es requerido"))
        }
        return repository.getLugares(tripId).map { lugares ->
            lugares.filter { it.tripId == tripId }
        }
    }
}
