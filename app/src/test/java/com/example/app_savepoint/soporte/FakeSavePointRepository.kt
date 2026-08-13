package com.example.app_savepoint.soporte

import com.example.app_savepoint.domain.model.Acento
import com.example.app_savepoint.domain.model.AjustesUsuario
import com.example.app_savepoint.domain.model.DetalleJuego
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.Objetivo
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import com.example.app_savepoint.domain.model.Sesion
import com.example.app_savepoint.domain.repository.SavePointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSavePointRepository : SavePointRepository {
    override val biblioteca = MutableStateFlow<List<JuegoBiblioteca>>(emptyList())
    override val sesiones = MutableStateFlow<List<Sesion>>(emptyList())
    override val ajustes = MutableStateFlow(AjustesUsuario())
    val objetivos = MutableStateFlow<List<Objetivo>>(emptyList())

    var catalogo: Result<List<Juego>> = Result.success(emptyList())
    var detalle: Result<DetalleJuego>? = null

    override suspend fun obtenerCatalogo(): List<Juego> = catalogo.getOrThrow()
    override suspend fun obtenerDetalle(juegoId: Int): DetalleJuego = requireNotNull(detalle).getOrThrow()
    override fun observarObjetivos(juegoId: Int): Flow<List<Objetivo>> = objetivos
    override suspend fun guardarJuego(juego: Juego) = Unit
    override suspend fun actualizarProgreso(juego: JuegoBiblioteca, progreso: Int) = Unit
    override suspend fun eliminarJuego(juego: JuegoBiblioteca) = Unit
    override suspend fun agregarObjetivo(juegoId: Int, descripcion: String) = Unit
    override suspend fun alternarObjetivo(objetivo: Objetivo) = Unit
    override suspend fun registrarSesion(
        juego: JuegoBiblioteca,
        duracionMinutos: Int,
        progreso: Int,
        nota: String,
        fotoUri: String?
    ) = Unit
    override suspend fun eliminarSesion(sesion: Sesion) = Unit
    override suspend fun guardarAcento(acento: Acento) = Unit
    override suspend fun guardarOrden(orden: OrdenBiblioteca) = Unit
}
