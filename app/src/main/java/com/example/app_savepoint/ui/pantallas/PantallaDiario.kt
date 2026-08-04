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
fun PantallaDiario() {
    Column(Modifier.fillMaxSize()) {
        EncabezadoSavePoint()
        Text("Diario", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(16.dp))
        EstadoVacio("Aún no hay sesiones", "Registra una sesión para recordar qué jugaste y cuánto avanzaste.")
    }
}
