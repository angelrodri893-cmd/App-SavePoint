package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_savepoint.domain.model.EstadoJuego
import com.example.app_savepoint.data.local.JuegoDao
import com.example.app_savepoint.data.local.JuegoGuardado
import com.example.app_savepoint.data.local.ObjetivoDao
import com.example.app_savepoint.data.local.ObjetivoJuego
import com.example.app_savepoint.data.remote.FreeToGameApi
import com.example.app_savepoint.data.mapper.aDominio
import com.example.app_savepoint.domain.model.DetalleJuego
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.ui.estado.LoadState
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JuegoViewModel(
    private val juegoDao: JuegoDao,
    private val objetivoDao: ObjetivoDao,
    private val api: FreeToGameApi
) : ViewModel() {
    val biblioteca: StateFlow<List<JuegoGuardado>> = juegoDao.observarBiblioteca()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _catalogo = MutableStateFlow<LoadState<List<Juego>>>(LoadState.Loading)
    val catalogo = _catalogo.asStateFlow()

    private val _detalle = MutableStateFlow<LoadState<DetalleJuego>?>(null)
    val detalle = _detalle.asStateFlow()

    init {
        cargarCatalogo()
    }

    fun cargarCatalogo() {
        viewModelScope.launch {
            _catalogo.value = LoadState.Loading
            _catalogo.value = try {
                LoadState.Content(api.obtenerJuegos().map { it.aDominio() })
            } catch (error: Exception) {
                LoadState.Error(mensajeDe(error))
            }
        }
    }

    fun cargarDetalle(juegoId: Int) {
        viewModelScope.launch {
            _detalle.value = LoadState.Loading
            _detalle.value = try {
                LoadState.Content(api.obtenerDetalle(juegoId).aDominio())
            } catch (error: Exception) {
                LoadState.Error(mensajeDe(error))
            }
        }
    }

    fun limpiarDetalle() {
        _detalle.value = null
    }

    fun observarObjetivos(juegoId: Int) = objetivoDao.observarObjetivos(juegoId)

    fun guardar(juego: Juego) {
        viewModelScope.launch {
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
    }

    fun actualizarProgreso(juego: JuegoGuardado, progreso: Int) {
        viewModelScope.launch {
            val limitado = progreso.coerceIn(0, 100)
            juegoDao.actualizar(
                juego.copy(
                    progreso = limitado,
                    estado = when {
                        limitado >= 100 -> EstadoJuego.COMPLETADO
                        limitado > 0 -> EstadoJuego.JUGANDO
                        else -> juego.estado
                    }
                )
            )
        }
    }

    fun eliminar(juego: JuegoGuardado) = viewModelScope.launch { juegoDao.eliminar(juego) }

    fun agregarObjetivo(juegoId: Int, descripcion: String) {
        if (descripcion.isBlank()) return
        viewModelScope.launch {
            objetivoDao.guardar(ObjetivoJuego(juegoId = juegoId, descripcion = descripcion.trim()))
        }
    }

    fun alternarObjetivo(objetivo: ObjetivoJuego) {
        viewModelScope.launch { objetivoDao.actualizar(objetivo.copy(completado = !objetivo.completado)) }
    }

    private fun mensajeDe(error: Exception): String = when (error) {
        is IOException -> "No hay conexión. Revisa tu red e inténtalo de nuevo."
        else -> "No se pudieron cargar los juegos. Inténtalo de nuevo."
    }
}
