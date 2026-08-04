package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_savepoint.ui.componentes.EncabezadoSavePoint
import com.example.app_savepoint.ui.componentes.EstadoVacio

@Composable
fun PantallaBiblioteca() {
    Column(Modifier.fillMaxSize()) {
        EncabezadoSavePoint()
        Text("Mi biblioteca", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(16.dp))
        EstadoVacio("Tu biblioteca está vacía", "Agrega juegos desde Explorar para seguir tu progreso.")
    }
}
