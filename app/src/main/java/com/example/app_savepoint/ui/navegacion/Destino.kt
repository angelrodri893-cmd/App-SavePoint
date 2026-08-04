package com.example.app_savepoint.ui.navegacion

import androidx.annotation.DrawableRes
import com.example.app_savepoint.R

sealed class Destino(
    val ruta: String,
    val etiqueta: String,
    @param:DrawableRes val iconoActivo: Int,
    @param:DrawableRes val iconoInactivo: Int
) {
    data object Explorar : Destino("explorar", "Explorar", R.drawable.ic_explorar_completo, R.drawable.ic_explorar_trazado)
    data object Biblioteca : Destino("biblioteca", "Biblioteca", R.drawable.ic_control_completo, R.drawable.ic_control_trazado)
    data object Diario : Destino("diario", "Diario", R.drawable.ic_diario_completo, R.drawable.ic_diario_trazado)
    data object Ajustes : Destino("ajustes", "Ajustes", R.drawable.ic_ajustes_completo, R.drawable.ic_ajustes_trazado)

    companion object {
        val principales = listOf(Explorar, Biblioteca, Diario, Ajustes)
        const val DETALLE = "detalle/{juegoId}"
        fun detalle(juegoId: Int) = "detalle/$juegoId"
    }
}
