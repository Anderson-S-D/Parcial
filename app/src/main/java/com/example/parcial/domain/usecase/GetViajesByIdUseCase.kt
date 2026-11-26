package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.repository.ViajeRepository

class GetViajeUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(tripId: String): Result<Viaje> {
        if (tripId.isBlank()) {
            return Result.failure(IllegalArgumentException("El id del viaje es requerido"))
        }
        return repository.getViaje(tripId)
    }
}
