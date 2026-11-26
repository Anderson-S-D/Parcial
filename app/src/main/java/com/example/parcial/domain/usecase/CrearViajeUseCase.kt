package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.Viaje
import com.example.preparcial.domain.repository.ViajeRepository
import java.time.LocalDate

class CrearViajeUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(
        nombre: String,
        destino: String,
        fecha_inicio: LocalDate?,
        fecha_fin: LocalDate?
    ): Result<Viaje> {
        if (nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }
        if (destino.isBlank()) {
            return Result.failure(IllegalArgumentException("El destino no puede estar vacío"))
        }
        if (fecha_inicio == null) {
            return Result.failure(IllegalArgumentException("La fecha inicio no puede estar vacía"))
        }
        if (fecha_fin == null) {
            return Result.failure(IllegalArgumentException("La fecha fin no puede estar vacía"))
        }
        if (fecha_fin.isBefore(fecha_inicio)) {
            return Result.failure(IllegalArgumentException("La fecha fin debe ser igual o posterior a la fecha de inicio"))
        }
        return repository.crearViaje(nombre.trim(), destino.trim(), fecha_inicio, fecha_fin)
    }
}
