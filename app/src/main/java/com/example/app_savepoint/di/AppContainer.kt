package com.example.app_savepoint.di

import android.content.Context
import com.example.app_savepoint.data.local.PreferenciasUsuarioDataStore
import com.example.app_savepoint.data.local.SavePointDatabase
import com.example.app_savepoint.data.remote.FreeToGameClient
import com.example.app_savepoint.data.repository.SavePointRepositoryImpl
import com.example.app_savepoint.domain.repository.SavePointRepository

class AppContainer(contexto: Context) {
    private val baseDatos = SavePointDatabase.obtener(contexto)
    private val preferencias = PreferenciasUsuarioDataStore(contexto)

    val repository: SavePointRepository = SavePointRepositoryImpl(
        juegoDao = baseDatos.juegoDao(),
        sesionDao = baseDatos.sesionDao(),
        objetivoDao = baseDatos.objetivoDao(),
        preferencias = preferencias,
        api = FreeToGameClient.api
    )
}
