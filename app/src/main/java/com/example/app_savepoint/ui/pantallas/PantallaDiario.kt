package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.app_savepoint.data.local.JuegoGuardado
import com.example.app_savepoint.data.local.SesionJuego
import com.example.app_savepoint.ui.componentes.EncabezadoSavePoint
import com.example.app_savepoint.ui.componentes.EstadoVacio
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun PantallaDiario(
    sesiones: List<SesionJuego>,
    biblioteca: List<JuegoGuardado>,
    alRegistrar: (JuegoGuardado, Int, Int, String) -> Unit
) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        EncabezadoSavePoint()
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Diario", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.weight(1f))
            if (biblioteca.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { mostrarFormulario = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Sesión") }
                )
            }
        }
        if (sesiones.isEmpty()) {
            EstadoVacio("Aún no hay sesiones", "Registra una sesión para recordar qué jugaste y cuánto avanzaste.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(sesiones, key = { it.sesionId }) { sesion -> TarjetaSesion(sesion) }
            }
        }
    }
    if (mostrarFormulario) {
        FormularioSesion(
            juego = biblioteca.first(),
            alCerrar = { mostrarFormulario = false },
            alGuardar = { duracion, progreso, nota ->
                alRegistrar(biblioteca.first(), duracion, progreso, nota)
                mostrarFormulario = false
            }
        )
    }
}

@Composable
private fun TarjetaSesion(sesion: SesionJuego) {
    val fecha = remember(sesion.fecha) {
        Instant.ofEpochMilli(sesion.fecha).atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(sesion.tituloJuego, style = MaterialTheme.typography.titleLarge)
                Text("${sesion.progreso}%", color = MaterialTheme.colorScheme.secondary)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                Text("$fecha · ${sesion.duracionMinutos} min", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (sesion.nota.isNotBlank()) Text(sesion.nota, modifier = Modifier.padding(top = 12.dp))
        }
    }
}

@Composable
private fun FormularioSesion(
    juego: JuegoGuardado,
    alCerrar: () -> Unit,
    alGuardar: (Int, Int, String) -> Unit
) {
    var duracion by remember { mutableStateOf("60") }
    var progreso by remember { mutableStateOf(juego.progreso.toString()) }
    var nota by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = alCerrar,
        title = { Text("Nueva sesión · ${juego.titulo}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = duracion,
                    onValueChange = { duracion = it.filter(Char::isDigit) },
                    label = { Text("Duración en minutos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = progreso,
                    onValueChange = { progreso = it.filter(Char::isDigit) },
                    label = { Text("Progreso actual") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = nota, onValueChange = { nota = it }, label = { Text("Nota") })
            }
        },
        confirmButton = {
            TextButton(onClick = { alGuardar(duracion.toIntOrNull() ?: 1, progreso.toIntOrNull() ?: 0, nota) }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = alCerrar) { Text("Cancelar") } }
    )
}
