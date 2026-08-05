package com.example.app_savepoint.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface FreeToGameApi {
    @GET("games")
    suspend fun obtenerJuegos(): List<JuegoRemotoDto>

    @GET("game")
    suspend fun obtenerDetalle(@Query("id") juegoId: Int): JuegoDetalleDto
}
