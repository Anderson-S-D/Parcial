package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.Categoria
import com.example.parcial.domain.repository.ViajeRepository

class GetCategoriasUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(): Result<List<Categoria>> {
        return repository.getCategorias()
    }
}
