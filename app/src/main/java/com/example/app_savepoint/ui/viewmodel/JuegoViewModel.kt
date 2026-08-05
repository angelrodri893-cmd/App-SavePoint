package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_savepoint.data.local.EstadoJuego
import com.example.app_savepoint.data.local.JuegoDao
import com.example.app_savepoint.data.local.JuegoGuardado
import com.example.app_savepoint.data.local.ObjetivoDao
import com.example.app_savepoint.data.local.ObjetivoJuego
import com.example.app_savepoint.ui.modelo.JuegoVista
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JuegoViewModel(
    private val juegoDao: JuegoDao,
    private val objetivoDao: ObjetivoDao
) : ViewModel() {
    val biblioteca: StateFlow<List<JuegoGuardado>> = juegoDao.observarBiblioteca()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun observarObjetivos(juegoId: Int) = objetivoDao.observarObjetivos(juegoId)

    fun guardar(juego: JuegoVista) {
        viewModelScope.launch {
            juegoDao.guardar(
                JuegoGuardado(
                    juegoId = juego.id,
                    titulo = juego.titulo,
                    imagenUrl = "",
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
}
