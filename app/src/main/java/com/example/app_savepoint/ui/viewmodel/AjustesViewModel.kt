package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_savepoint.domain.model.Acento
import com.example.app_savepoint.domain.model.AjustesUsuario
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import com.example.app_savepoint.domain.repository.SavePointRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesViewModel(private val repository: SavePointRepository) : ViewModel() {
    val ajustes: StateFlow<AjustesUsuario> = repository.ajustes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AjustesUsuario())

    fun seleccionarAcento(acento: Acento) {
        viewModelScope.launch { repository.guardarAcento(acento) }
    }

    fun seleccionarOrden(orden: OrdenBiblioteca) {
        viewModelScope.launch { repository.guardarOrden(orden) }
    }
}
