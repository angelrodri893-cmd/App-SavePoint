package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_savepoint.data.local.ObjetivoJuego
import com.example.app_savepoint.ui.modelo.JuegoVista

@Composable
fun PantallaDetalle(
    juego: JuegoVista,
    guardado: Boolean,
    objetivos: List<ObjetivoJuego>,
    alVolver: () -> Unit,
    alGuardar: () -> Unit,
    alAgregarObjetivo: (String) -> Unit,
    alAlternarObjetivo: (ObjetivoJuego) -> Unit
) {
    var mostrarObjetivo by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier.fillMaxWidth().height(260.dp).background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SportsEsports, contentDescription = null, modifier = Modifier.size(84.dp), tint = MaterialTheme.colorScheme.primary)
            IconButton(onClick = alVolver, modifier = Modifier.align(Alignment.TopStart).padding(12.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
        Column(Modifier.padding(16.dp)) {
            Text(juego.titulo, style = MaterialTheme.typography.headlineLarge)
            Text(juego.genero, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(20.dp))
            Button(onClick = alGuardar, modifier = Modifier.fillMaxWidth(), enabled = !guardado) {
                Icon(if (guardado) Icons.Default.CheckCircle else Icons.Default.Add, contentDescription = null)
                Text(if (guardado) "  Guardado en mi biblioteca" else "  Agregar a mi biblioteca")
            }
            Spacer(Modifier.height(28.dp))
            Text("Acerca del juego", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text(
                "Consulta la información del videojuego, guárdalo en tu biblioteca y registra tu progreso personal.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Text("  Objetivos personales", style = MaterialTheme.typography.titleLarge)
                        }
                        IconButton(onClick = { mostrarObjetivo = true }, enabled = guardado) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar objetivo")
                        }
                    }
                    if (!guardado) {
                        Text("Guarda el juego para crear objetivos.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else if (objetivos.isEmpty()) {
                        Text("Todavía no tienes objetivos para este juego.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        objetivos.forEach { objetivo ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = objetivo.completado, onCheckedChange = { alAlternarObjetivo(objetivo) })
                                Text(objetivo.descripcion)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
            Text(
                "Datos proporcionados por FreeToGame",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
        }
    }
    if (mostrarObjetivo) {
        DialogoObjetivo(
            alCerrar = { mostrarObjetivo = false },
            alGuardar = {
                alAgregarObjetivo(it)
                mostrarObjetivo = false
            }
        )
    }
}

@Composable
private fun DialogoObjetivo(alCerrar: () -> Unit, alGuardar: (String) -> Unit) {
    var descripcion by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = alCerrar,
        title = { Text("Nuevo objetivo") },
        text = {
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                singleLine = true
            )
        },
        confirmButton = { TextButton(onClick = { alGuardar(descripcion) }, enabled = descripcion.isNotBlank()) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = alCerrar) { Text("Cancelar") } }
    )
}
