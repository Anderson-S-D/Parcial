package com.example.preparcial.domain.usecase

import androidx.annotation.Nullable
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
        if (fecha_inicio==null) {
            return Result.failure(IllegalArgumentException("La fecha inicio no puede estar vacía"))
        }
        if (fecha_fin==null) {
            return Result.failure(IllegalArgumentException("La fecha fin no puede estar vacío"))
        }
        return repository.crearViaje(nombre.trim(), destino, fecha_inicio, fecha_fin)
    }
}
