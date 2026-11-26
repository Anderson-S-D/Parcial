package com.example.parcial.domain.model

import java.time.LocalDate

data class Lugar(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val ciudad: String,
    val categoria: Categoria? = null,
    val fechaTentativa: LocalDate,
    val tripId: String = "",
    val categoriaId: Int? = null
)
