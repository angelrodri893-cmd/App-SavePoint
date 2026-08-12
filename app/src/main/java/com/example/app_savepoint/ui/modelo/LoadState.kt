package com.example.app_savepoint.ui.modelo

sealed interface LoadState<out T> {
    data object Loading : LoadState<Nothing>
    data class Content<T>(val data: T) : LoadState<T>
    data class Error(val mensaje: String) : LoadState<Nothing>
}
