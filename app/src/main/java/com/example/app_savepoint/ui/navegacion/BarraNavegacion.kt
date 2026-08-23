package com.example.app_savepoint.ui.navegacion

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BarraNavegacion(
    rutaActual: String?,
    alNavegar: (Destino) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        Destino.principales.forEach { destino ->
            val seleccionado = rutaActual == destino.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = { alNavegar(destino) },
                icon = {
                    Icon(
                        imageVector = if (seleccionado) destino.iconoActivo else destino.iconoInactivo,
                        contentDescription = destino.etiqueta,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(destino.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
