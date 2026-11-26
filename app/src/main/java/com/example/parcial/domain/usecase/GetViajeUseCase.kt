package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.Viaje
import com.example.parcial.domain.repository.ViajeRepository

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
