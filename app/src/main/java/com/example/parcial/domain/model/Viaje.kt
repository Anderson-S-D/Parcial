package com.example.parcial.domain.model

import java.time.LocalDate

data class Viaje(
    val id: String,
    val nombre: String,
    val destino: String,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate
)
