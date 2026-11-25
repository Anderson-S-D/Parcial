package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.repository.ViajeRepository
import kotlinx.coroutines.flow.Flow

class GetViajesByIdUseCase(
    private val repository: ViajeRepository
) {
    operator fun invoke(id: String): Flow<Viaje?> {
        return repository.getViajesById(id)
    }
}
