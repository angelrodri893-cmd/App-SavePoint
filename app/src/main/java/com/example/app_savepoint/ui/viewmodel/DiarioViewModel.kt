package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_savepoint.data.local.SesionDao
import com.example.app_savepoint.data.local.SesionJuego
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiarioViewModel(private val sesionDao: SesionDao) : ViewModel() {
    val sesiones: StateFlow<List<SesionJuego>> = sesionDao.observarSesiones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun registrar(
        juegoId: Int,
        tituloJuego: String,
        duracionMinutos: Int,
        progreso: Int,
        nota: String,
        fotoUri: String? = null
    ) {
        viewModelScope.launch {
            sesionDao.insertar(
                SesionJuego(
                    juegoId = juegoId,
                    tituloJuego = tituloJuego,
                    duracionMinutos = duracionMinutos.coerceAtLeast(1),
                    progreso = progreso.coerceIn(0, 100),
                    nota = nota.trim(),
                    fotoUri = fotoUri
                )
            )
        }
    }

    fun eliminar(sesion: SesionJuego) = viewModelScope.launch { sesionDao.eliminar(sesion) }
}
