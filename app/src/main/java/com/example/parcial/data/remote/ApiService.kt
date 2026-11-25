package com.example.parcial.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("api/trips")
    suspend fun getViaje(): Response<ApiResponse<List<PlanDto>>>

    @GET("api/trips/{id}")
    suspend fun getViajeById(@Path("id") id: String): Response<ApiResponse<PlanDto>>

    @POST("api/trips")
    suspend fun crearViaje(@Body request: CrearViajeRequestDto): Response<ApiResponse<PlanDto>>

    @GET("api/places")
    suspend fun getLugares(): Response<ApiResponse<List<PlanDto>>>

    @POST("api/places")
    suspend fun crearLugar(@Body request: CrearLugarRequestDto): Response<ApiResponse<PlanDto>>

    @GET("api/categories")
    suspend fun getCategorias(): Response<ApiResponse<List<PlanDto>>>
