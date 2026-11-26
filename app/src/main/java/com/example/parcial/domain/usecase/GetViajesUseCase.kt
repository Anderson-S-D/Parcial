package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.repository.ViajeRepository

class GetViajesUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(): Result<List<Viaje>> {
        return repository.getViajes()
    }
}
