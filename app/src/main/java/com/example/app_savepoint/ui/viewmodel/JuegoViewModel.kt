package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_savepoint.domain.model.DetalleJuego
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.Objetivo
import com.example.app_savepoint.domain.repository.SavePointRepository
import com.example.app_savepoint.ui.estado.LoadState
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JuegoViewModel(private val repository: SavePointRepository) : ViewModel() {
    val biblioteca: StateFlow<List<JuegoBiblioteca>> = repository.biblioteca
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
                LoadState.Content(repository.obtenerCatalogo())
            } catch (error: Exception) {
                LoadState.Error(mensajeDe(error))
            }
        }
    }

    fun cargarDetalle(juegoId: Int) {
        viewModelScope.launch {
            _detalle.value = LoadState.Loading
            _detalle.value = try {
                LoadState.Content(repository.obtenerDetalle(juegoId))
            } catch (error: Exception) {
                LoadState.Error(mensajeDe(error))
            }
        }
    }

    fun limpiarDetalle() {
        _detalle.value = null
    }

    fun observarObjetivos(juegoId: Int) = repository.observarObjetivos(juegoId)

    fun guardar(juego: Juego) = viewModelScope.launch { repository.guardarJuego(juego) }

    fun actualizarProgreso(juego: JuegoBiblioteca, progreso: Int) = viewModelScope.launch {
        repository.actualizarProgreso(juego, progreso)
    }

    fun eliminar(juego: JuegoBiblioteca) = viewModelScope.launch { repository.eliminarJuego(juego) }

    fun agregarObjetivo(juegoId: Int, descripcion: String) = viewModelScope.launch {
        repository.agregarObjetivo(juegoId, descripcion)
    }

    fun alternarObjetivo(objetivo: Objetivo) = viewModelScope.launch {
        repository.alternarObjetivo(objetivo)
    }

    private fun mensajeDe(error: Exception): String = when (error) {
        is IOException -> "No hay conexión. Revisa tu red e inténtalo de nuevo."
        else -> "No se pudieron cargar los juegos. Inténtalo de nuevo."
    }
}
