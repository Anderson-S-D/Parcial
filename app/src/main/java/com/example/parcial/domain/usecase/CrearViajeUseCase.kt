package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.Viaje
import com.example.parcial.domain.repository.ViajeRepository
import java.time.LocalDate

class CrearViajeUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(
        nombre: String,
        destino: String,
        fechaInicio: LocalDate?,
        fechaFin: LocalDate?
    ): Result<Viaje> {
        if (nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }
        if (destino.isBlank()) {
            return Result.failure(IllegalArgumentException("El destino no puede estar vacío"))
        }
        if (fechaInicio == null) {
            return Result.failure(IllegalArgumentException("La fecha inicio no puede estar vacía"))
        }
        if (fechaFin == null) {
            return Result.failure(IllegalArgumentException("La fecha fin no puede estar vacía"))
        }
        if (fechaFin.isBefore(fechaInicio)) {
            return Result.failure(IllegalArgumentException("La fecha fin debe ser igual o posterior a la fecha de inicio"))
        }
        return repository.crearViaje(nombre.trim(), destino.trim(), fechaInicio, fechaFin)
    }
}
