package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.Viaje
import com.example.parcial.domain.repository.ViajeRepository

class GetViajesUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(): Result<List<Viaje>> {
        return repository.getViajes()
    }
}
