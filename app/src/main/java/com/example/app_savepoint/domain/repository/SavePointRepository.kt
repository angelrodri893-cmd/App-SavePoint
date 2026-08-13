package com.example.app_savepoint.domain.repository

import com.example.app_savepoint.domain.model.Acento
import com.example.app_savepoint.domain.model.AjustesUsuario
import com.example.app_savepoint.domain.model.DetalleJuego
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.Objetivo
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import com.example.app_savepoint.domain.model.Sesion
import kotlinx.coroutines.flow.Flow

interface SavePointRepository {
    val biblioteca: Flow<List<JuegoBiblioteca>>
    val sesiones: Flow<List<Sesion>>
    val ajustes: Flow<AjustesUsuario>

    suspend fun obtenerCatalogo(): List<Juego>
    suspend fun obtenerDetalle(juegoId: Int): DetalleJuego

    fun observarObjetivos(juegoId: Int): Flow<List<Objetivo>>
    suspend fun guardarJuego(juego: Juego)
    suspend fun actualizarProgreso(juego: JuegoBiblioteca, progreso: Int)
    suspend fun eliminarJuego(juego: JuegoBiblioteca)

    suspend fun agregarObjetivo(juegoId: Int, descripcion: String)
    suspend fun alternarObjetivo(objetivo: Objetivo)

    suspend fun registrarSesion(
        juego: JuegoBiblioteca,
        duracionMinutos: Int,
        progreso: Int,
        nota: String,
        fotoUri: String? = null
    )

    suspend fun eliminarSesion(sesion: Sesion)
    suspend fun guardarAcento(acento: Acento)
    suspend fun guardarOrden(orden: OrdenBiblioteca)
}
