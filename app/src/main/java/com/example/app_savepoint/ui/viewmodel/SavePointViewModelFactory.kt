package com.example.app_savepoint.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_savepoint.domain.repository.SavePointRepository

class SavePointViewModelFactory(private val repository: SavePointRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(JuegoViewModel::class.java) -> JuegoViewModel(repository) as T
        modelClass.isAssignableFrom(DiarioViewModel::class.java) -> DiarioViewModel(repository) as T
        modelClass.isAssignableFrom(AjustesViewModel::class.java) -> AjustesViewModel(repository) as T
        else -> error("ViewModel no registrado: ${modelClass.name}")
    }
}
