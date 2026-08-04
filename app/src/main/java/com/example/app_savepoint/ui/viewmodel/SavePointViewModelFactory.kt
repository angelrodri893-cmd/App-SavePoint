package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_savepoint.data.local.SavePointDatabase

class SavePointViewModelFactory(private val baseDatos: SavePointDatabase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(JuegoViewModel::class.java) ->
            JuegoViewModel(baseDatos.juegoDao(), baseDatos.objetivoDao()) as T
        modelClass.isAssignableFrom(DiarioViewModel::class.java) ->
            DiarioViewModel(baseDatos.sesionDao()) as T
        else -> error("ViewModel no registrado: ${modelClass.name}")
    }
}
