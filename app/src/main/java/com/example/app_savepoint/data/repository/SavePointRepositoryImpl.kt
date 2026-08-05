package com.example.app_savepoint.data.repository

import com.example.app_savepoint.data.local.JuegoDao
import com.example.app_savepoint.data.local.JuegoGuardado
import com.example.app_savepoint.data.local.ObjetivoDao
import com.example.app_savepoint.data.local.ObjetivoJuego
import com.example.app_savepoint.data.local.PreferenciasUsuarioDataStore
import com.example.app_savepoint.data.local.SesionDao
import com.example.app_savepoint.data.local.SesionJuego
import com.example.app_savepoint.data.mapper.aDominio
import com.example.app_savepoint.data.mapper.aEntidad
import com.example.app_savepoint.data.remote.FreeToGameApi
import com.example.app_savepoint.domain.model.Acento
import com.example.app_savepoint.domain.model.AjustesUsuario
import com.example.app_savepoint.domain.model.DetalleJuego
import com.example.app_savepoint.domain.model.EstadoJuego
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.Objetivo
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import com.example.app_savepoint.domain.model.Sesion
import com.example.app_savepoint.domain.repository.SavePointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavePointRepositoryImpl(
    private val juegoDao: JuegoDao,
    private val sesionDao: SesionDao,
    private val objetivoDao: ObjetivoDao,
    private val preferencias: PreferenciasUsuarioDataStore,
    private val api: FreeToGameApi
) : SavePointRepository {
    override val biblioteca: Flow<List<JuegoBiblioteca>> =
        juegoDao.observarBiblioteca().map { juegos -> juegos.map { it.aDominio() } }

    override val sesiones: Flow<List<Sesion>> =
        sesionDao.observarSesiones().map { sesiones -> sesiones.map { it.aDominio() } }

    override val ajustes: Flow<AjustesUsuario> = preferencias.ajustes

    override suspend fun obtenerCatalogo(): List<Juego> = api.obtenerJuegos().map { it.aDominio() }

    override suspend fun obtenerDetalle(juegoId: Int): DetalleJuego = api.obtenerDetalle(juegoId).aDominio()

    override fun observarObjetivos(juegoId: Int): Flow<List<Objetivo>> =
        objetivoDao.observarObjetivos(juegoId).map { objetivos -> objetivos.map { it.aDominio() } }

    override suspend fun guardarJuego(juego: Juego) {
        juegoDao.guardar(
            JuegoGuardado(
                juegoId = juego.id,
                titulo = juego.titulo,
                imagenUrl = juego.imagenUrl,
                genero = juego.genero,
                plataforma = juego.plataforma
            )
        )
    }

    override suspend fun actualizarProgreso(juego: JuegoBiblioteca, progreso: Int) {
        val limitado = progreso.coerceIn(0, 100)
        juegoDao.actualizar(
            juego.copy(
                progreso = limitado,
                estado = when {
                    limitado >= 100 -> EstadoJuego.COMPLETADO
                    limitado > 0 -> EstadoJuego.JUGANDO
                    else -> juego.estado
                }
            ).aEntidad()
        )
    }

    override suspend fun eliminarJuego(juego: JuegoBiblioteca) {
        juegoDao.eliminar(juego.aEntidad())
    }

    override suspend fun agregarObjetivo(juegoId: Int, descripcion: String) {
        if (descripcion.isNotBlank()) {
            objetivoDao.guardar(ObjetivoJuego(juegoId = juegoId, descripcion = descripcion.trim()))
        }
    }

    override suspend fun alternarObjetivo(objetivo: Objetivo) {
        objetivoDao.actualizar(objetivo.copy(completado = !objetivo.completado).aEntidad())
    }

    override suspend fun registrarSesion(
        juego: JuegoBiblioteca,
        duracionMinutos: Int,
        progreso: Int,
        nota: String,
        fotoUri: String?
    ) {
        sesionDao.insertar(
            SesionJuego(
                juegoId = juego.juegoId,
                tituloJuego = juego.titulo,
                duracionMinutos = duracionMinutos.coerceAtLeast(1),
                progreso = progreso.coerceIn(0, 100),
                nota = nota.trim(),
                fotoUri = fotoUri
            )
        )
        actualizarProgreso(juego, progreso)
    }

    override suspend fun eliminarSesion(sesion: Sesion) {
        sesionDao.eliminar(sesion.aEntidad())
    }

    override suspend fun guardarAcento(acento: Acento) = preferencias.guardarAcento(acento)

    override suspend fun guardarOrden(orden: OrdenBiblioteca) = preferencias.guardarOrden(orden)
}
