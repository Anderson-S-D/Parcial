package com.example.parcial.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/trips")
    suspend fun getViajes(): Response<List<ViajeDto>>

    @GET("api/trips/{id}")
    suspend fun getViajeById(@Path("id") id: String): Response<ViajeDto>

    @POST("api/trips")
    suspend fun crearViaje(@Body request: CrearViajeRequestDto): Response<ViajeDto>

    @GET("api/places")
    suspend fun getLugares(@Query("tripId") viajeId: String? = null): Response<List<LugarDto>>

    @POST("api/places")
    suspend fun crearLugar(@Body request: CrearLugarRequestDto): Response<LugarDto>

    @GET("api/categories")
    suspend fun getCategorias(): Response<List<CategoriaDto>>
}
