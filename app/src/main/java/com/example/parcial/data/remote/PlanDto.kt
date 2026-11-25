package com.example.parcial.data.remote

import com.example.preparcial.domain.model.Categoria
import com.example.preparcial.domain.model.Lugar
import com.example.preparcial.domain.model.Viaje
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import kotlin.String


data class CategoriaDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("nombre") val nombre: String? = null
) {
    fun toDomain(): Categoria {
        return Categoria(
            id = id ?: "",
            nombre = nombre ?: ""
        )
    }
}

data class ViajeDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("destino") val destino: String? = null,
    @SerializedName("fecha_inicio") val fecha_inicio: String? = null,
    @SerializedName("fecha_fin") val fecha_fin: String? = null
) {
    fun toDomain(): Viaje {
        return Viaje(
            id = id ?: "",
            nombre = nombre ?: "",
            destino= String ?: "",
            fecha_inicio= LocalDate?: "",
            fecha_fin= LocalDate: ""
        )
    }
}

data class CrearViajeRequestDto(
    @SerializedName("motivo") val motivo: String,
    @SerializedName("meta") val meta: Double,
    @SerializedName("meses") val meses: Int,
    @SerializedName("miembros") val miembros: List<String>
)

data class CrearLugarRequestDto(
    @SerializedName("motivo") val motivo: String,
    @SerializedName("meta") val meta: Double,
    @SerializedName("meses") val meses: Int,
    @SerializedName("miembros") val miembros: List<String>
)

