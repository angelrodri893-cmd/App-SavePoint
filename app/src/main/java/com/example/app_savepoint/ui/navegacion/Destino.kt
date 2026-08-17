package com.example.app_savepoint.ui.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destino(
    val ruta: String,
    val etiqueta: String,
    val iconoActivo: ImageVector,
    val iconoInactivo: ImageVector
) {
    data object Explorar : Destino("explorar", "Explorar", Icons.Filled.Explore, Icons.Outlined.Explore)
    data object Biblioteca : Destino("biblioteca", "Biblioteca", Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports)
    data object Diario : Destino("diario", "Diario", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder)
    data object Ajustes : Destino("ajustes", "Ajustes", Icons.Filled.Settings, Icons.Outlined.Settings)

    companion object {
        val principales = listOf(Explorar, Biblioteca, Diario, Ajustes)
        const val DETALLE = "detalle/{juegoId}"
        fun detalle(juegoId: Int) = "detalle/$juegoId"
    }
}
