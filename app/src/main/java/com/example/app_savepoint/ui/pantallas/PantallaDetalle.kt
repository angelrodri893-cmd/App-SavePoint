package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app_savepoint.domain.model.DetalleJuego
import com.example.app_savepoint.domain.model.Objetivo
import com.example.app_savepoint.ui.estado.LoadState

@Composable
fun PantallaDetalle(
    estado: LoadState<DetalleJuego>,
    guardado: Boolean,
    objetivos: List<Objetivo>,
    alVolver: () -> Unit,
    alReintentar: () -> Unit,
    alGuardar: (DetalleJuego) -> Unit,
    alAgregarObjetivo: (String) -> Unit,
    alAlternarObjetivo: (Objetivo) -> Unit
) {
    when (estado) {
        LoadState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            IconButton(onClick = alVolver, modifier = Modifier.align(Alignment.TopStart).padding(12.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
        is LoadState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Text(estado.mensaje)
                Button(onClick = alReintentar) { Text("Reintentar") }
                TextButton(onClick = alVolver) { Text("Volver") }
            }
        }
        is LoadState.Content -> ContenidoDetalle(
            detalle = estado.data,
            guardado = guardado,
            objetivos = objetivos,
            alVolver = alVolver,
            alGuardar = { alGuardar(estado.data) },
            alAgregarObjetivo = alAgregarObjetivo,
            alAlternarObjetivo = alAlternarObjetivo
        )
    }
}

@Composable
private fun ContenidoDetalle(
    detalle: DetalleJuego,
    guardado: Boolean,
    objetivos: List<Objetivo>,
    alVolver: () -> Unit,
    alGuardar: () -> Unit,
    alAgregarObjetivo: (String) -> Unit,
    alAlternarObjetivo: (Objetivo) -> Unit
) {
    val juego = detalle.juego
    var mostrarObjetivo by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().height(260.dp).background(MaterialTheme.colorScheme.surfaceVariant)) {
            Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center).size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            AsyncImage(
                model = detalle.capturas.firstOrNull() ?: juego.imagenUrl,
                contentDescription = "Imagen principal de ${juego.titulo}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = alVolver,
                modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = .8f), RoundedCornerShape(50))
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
        }
        Column(Modifier.padding(vertical = 16.dp)) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text(juego.titulo, style = MaterialTheme.typography.headlineLarge)
                Text(
                    listOf(juego.fechaLanzamiento, juego.genero, juego.plataforma).filter(String::isNotBlank).joinToString(" · "),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(20.dp))
                Button(onClick = alGuardar, modifier = Modifier.fillMaxWidth(), enabled = !guardado) {
                    Icon(if (guardado) Icons.Default.CheckCircle else Icons.Default.Add, contentDescription = null)
                    Text(if (guardado) "  Guardado en mi biblioteca" else "  Agregar a mi biblioteca")
                }
                Spacer(Modifier.height(28.dp))
                Text("Acerca del juego", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(detalle.descripcion.ifBlank { juego.descripcionCorta }, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (detalle.capturas.isNotEmpty()) {
                Text("Capturas", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(detalle.capturas) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "Captura de ${juego.titulo}",
                            modifier = Modifier.size(width = 260.dp, height = 150.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Column(Modifier.padding(16.dp)) {
                if (detalle.requisitos.isNotEmpty()) {
                    Text("Requisitos mínimos", style = MaterialTheme.typography.headlineSmall)
                    detalle.requisitos.forEach { (nombre, valor) ->
                        Text("$nombre: $valor", modifier = Modifier.padding(top = 6.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.height(24.dp))
                }
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
                Text(
                    "Datos proporcionados por FreeToGame",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 28.dp)
                        .clickable { uriHandler.openUri("https://www.freetogame.com/") },
                    color = MaterialTheme.colorScheme.primary
                )
            }
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
