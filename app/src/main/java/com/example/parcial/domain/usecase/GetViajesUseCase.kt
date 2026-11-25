package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.repository.ViajeRepository
import kotlinx.coroutines.flow.Flow

class GetViajesUseCase(
    private val repository: ViajeRepository
) {
    operator fun invoke(): Flow<List<Viaje>> {
        return repository.getViajes()
    }
}
