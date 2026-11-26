package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.Lugar
import com.example.parcial.domain.repository.ViajeRepository
import java.time.LocalDate

class RegistrarLugarUseCase(
    private val repository: ViajeRepository
) {
    suspend operator fun invoke(
        tripId: String,
        nombre: String,
        descripcion: String,
        ciudad: String,
        categoriaId: Int?,
        fechaTentativa: LocalDate?
    ): Result<Lugar> {
        if (tripId.isBlank()) {
            return Result.failure(IllegalArgumentException("No se identificó el viaje"))
        }
        if (nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del lugar es requerido"))
        }
        if (descripcion.isBlank()) {
            return Result.failure(IllegalArgumentException("La descripción es requerida"))
        }
        if (ciudad.isBlank()) {
            return Result.failure(IllegalArgumentException("La ciudad es requerida"))
        }
        if (categoriaId == null) {
            return Result.failure(IllegalArgumentException("Debe seleccionar una categoría"))
        }
        if (fechaTentativa == null) {
            return Result.failure(IllegalArgumentException("Debe ingresar la fecha tentativa"))
        }

        val resultadoViaje = repository.getViaje(tripId)
        if (resultadoViaje.isFailure) {
            return Result.failure(
                resultadoViaje.exceptionOrNull() ?: IllegalStateException("No se pudo consultar el viaje")
            )
        }
        val viaje = resultadoViaje.getOrThrow()

        if (fechaTentativa.isBefore(viaje.fechaInicio) || fechaTentativa.isAfter(viaje.fechaFin)) {
            return Result.failure(
                IllegalArgumentException("La fecha tentativa debe estar dentro del período del viaje")
            )
        }

        return repository.crearLugar(
            tripId = tripId,
            nombre = nombre.trim(),
            descripcion = descripcion.trim(),
            ciudad = ciudad.trim(),
            categoriaId = categoriaId,
            fechaTentativa = fechaTentativa
        )
    }
}
