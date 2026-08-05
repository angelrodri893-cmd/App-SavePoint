package com.example.app_savepoint.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FreeToGameClient {
    private const val BASE_URL = "https://www.freetogame.com/api/"

    val api: FreeToGameApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FreeToGameApi::class.java)
    }
}
