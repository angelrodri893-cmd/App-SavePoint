package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.Sesion
import com.example.app_savepoint.domain.repository.SavePointRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiarioViewModel(private val repository: SavePointRepository) : ViewModel() {
    val sesiones: StateFlow<List<Sesion>> = repository.sesiones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun registrar(
        juego: JuegoBiblioteca,
        duracionMinutos: Int,
        progreso: Int,
        nota: String,
        fotoUri: String? = null
    ) = viewModelScope.launch {
        repository.registrarSesion(juego, duracionMinutos, progreso, nota, fotoUri)
    }

    fun eliminar(sesion: Sesion) = viewModelScope.launch { repository.eliminarSesion(sesion) }
}
