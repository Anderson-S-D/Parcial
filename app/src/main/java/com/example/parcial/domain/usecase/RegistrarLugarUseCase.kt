package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Categoria
import com.example.preparcial.domain.repository.ViajeRepository
import java.time.LocalDate

class RegistrarLugarUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(
        nombre: String,
        descripcion: String,
        ciudad: String,
        categoria: List<Categoria>?,
        fecha_tentativa: LocalDate
    ): Result<Unit> {
        if (nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del lugar es requerido"))
        }
        if (ciudad.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre de la ciudad es requerido"))
        }
        if (categoria==null) {
            return Result.failure(IllegalArgumentException("Debe seleccionar por lo menos una categoria"))
        }
        if (fecha_tentativa==null) {
            return Result.failure(IllegalArgumentException("Debe ingresar la fecha tentativa"))
        }
        return repository.registrarLugar(nombre, descripcion, ciudad, categoria, fecha_tentativa)
    }
}
