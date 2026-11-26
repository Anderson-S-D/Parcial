package com.example.parcial.data.remote

import com.example.parcial.domain.model.Categoria
import com.example.parcial.domain.model.Lugar
import com.example.parcial.domain.model.Viaje
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class CategoriaDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val nombre: String? = null,
    @SerializedName("description") val descripcion: String? = null
) {
    fun toDomain(): Categoria {
        return Categoria(
            id = id ?: 0,
            nombre = nombre ?: ""
        )
    }
}

data class LugarDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("tripId") val viajeId: String? = null,
    @SerializedName("name") val nombre: String? = null,
    @SerializedName("description") val descripcion: String? = null,
    @SerializedName("city") val ciudad: String? = null,
    @SerializedName("categoryId") val categoriaId: Int? = null,
    @SerializedName("tentativeDate") val fechaTentativa: String? = null,
    @SerializedName("category") val categoria: CategoriaDto? = null
) {
    fun toDomain(): Lugar {
        return Lugar(
            id = id ?: "",
            nombre = nombre ?: "",
            descripcion = descripcion ?: "",
            ciudad = ciudad ?: "",
            categoria = categoria?.toDomain(),
            fechaTentativa = fechaTentativa?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                ?: LocalDate.MIN,
            tripId = viajeId.orEmpty(),
            categoriaId = categoriaId
        )
    }
}

data class ViajeDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val nombre: String? = null,
    @SerializedName("destination") val destino: String? = null,
    @SerializedName("startDate") val fechaInicio: String? = null,
    @SerializedName("endDate") val fechaFin: String? = null,
    @SerializedName("places") val lugares: List<LugarDto>? = null
) {
    fun toDomain(): Viaje {
        return Viaje(
            id = id ?: "",
            nombre = nombre ?: "",
            destino = destino ?: "",
            fechaInicio = fechaInicio?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                ?: LocalDate.MIN,
            fechaFin = fechaFin?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                ?: LocalDate.MIN
        )
    }
}

data class CrearViajeRequestDto(
    @SerializedName("name") val nombre: String,
    @SerializedName("destination") val destino: String,
    @SerializedName("startDate") val fechaInicio: String,
    @SerializedName("endDate") val fechaFin: String
)

data class CrearLugarRequestDto(
    @SerializedName("tripId") val viajeId: String,
    @SerializedName("name") val nombre: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("city") val ciudad: String,
    @SerializedName("categoryId") val categoriaId: Int,
    @SerializedName("tentativeDate") val fechaTentativa: String
)
