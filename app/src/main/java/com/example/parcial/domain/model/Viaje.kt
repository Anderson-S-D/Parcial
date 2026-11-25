package com.example.preparcial.domain.model

import java.time.LocalDate

data class Viaje(
    val id: String,
    val nombre: String,
    val destino: String,
    val fecha_inicio: LocalDate,
    val fecha_fin: LocalDate
)
