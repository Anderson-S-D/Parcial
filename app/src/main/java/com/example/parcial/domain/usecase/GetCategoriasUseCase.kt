package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Categoria
import com.example.preparcial.domain.repository.ViajeRepository

class GetCategoriasUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(): Result<List<Categoria>> {
        return repository.getCategorias()
    }
}
