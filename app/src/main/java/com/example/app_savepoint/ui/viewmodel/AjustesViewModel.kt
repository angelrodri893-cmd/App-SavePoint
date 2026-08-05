package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_savepoint.data.local.AjustesUsuario
import com.example.app_savepoint.data.local.OrdenBiblioteca
import com.example.app_savepoint.data.local.PreferenciasUsuarioDataStore
import com.example.app_savepoint.ui.theme.Acento
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesViewModel(private val preferencias: PreferenciasUsuarioDataStore) : ViewModel() {
    val ajustes: StateFlow<AjustesUsuario> = preferencias.ajustes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AjustesUsuario())

    fun seleccionarAcento(acento: Acento) {
        viewModelScope.launch { preferencias.guardarAcento(acento) }
    }

    fun seleccionarOrden(orden: OrdenBiblioteca) {
        viewModelScope.launch { preferencias.guardarOrden(orden) }
    }
}
