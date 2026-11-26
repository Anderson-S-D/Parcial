package com.example.preparcial.domain.model

import java.time.LocalDate

data class Lugar(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val ciudad: String,
    val categoria: List<Categoria> = emptyList(),
    val fecha_tentativa: LocalDate,
    val tripId: String = "",
    val categoriaId: Int? = null
)
