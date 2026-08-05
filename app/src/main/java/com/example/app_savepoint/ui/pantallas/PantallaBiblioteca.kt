package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app_savepoint.R
import com.example.app_savepoint.domain.model.EstadoJuego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.ui.componentes.EncabezadoSavePoint
import com.example.app_savepoint.ui.componentes.EstadoVacio

@Composable
fun PantallaBiblioteca(
    juegos: List<JuegoBiblioteca>,
    alCambiarProgreso: (JuegoBiblioteca, Int) -> Unit,
    alEliminar: (JuegoBiblioteca) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { EncabezadoSavePoint() }
        item {
            Text("Mi biblioteca", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(horizontal = 16.dp))
        }
        if (juegos.isEmpty()) {
            item { EstadoVacio("Tu biblioteca está vacía", "Agrega juegos desde Explorar para seguir tu progreso.") }
        } else {
            item { ResumenBiblioteca(juegos) }
            items(juegos, key = { it.juegoId }) { juego ->
                Card(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        if (juego.imagenUrl.isNotBlank()) {
                            AsyncImage(
                                model = juego.imagenUrl,
                                contentDescription = "Portada de ${juego.titulo}",
                                modifier = Modifier.fillMaxWidth().height(170.dp),
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.ic_control_completo)
                            )
                            Spacer(Modifier.height(14.dp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(juego.titulo, style = MaterialTheme.typography.titleLarge)
                                Text(juego.genero, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { alEliminar(juego) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar ${juego.titulo}")
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(juego.estado.name.lowercase().replaceFirstChar(Char::uppercase))
                            Text("${juego.progreso}%", color = MaterialTheme.colorScheme.primary)
                        }
                        LinearProgressIndicator(
                            progress = { juego.progreso / 100f },
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
                        )
                        Slider(
                            value = juego.progreso.toFloat(),
                            onValueChange = { alCambiarProgreso(juego, it.toInt()) },
                            valueRange = 0f..100f
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumenBiblioteca(juegos: List<JuegoBiblioteca>) {
    val jugando = juegos.count { it.estado == EstadoJuego.JUGANDO }
    val pendientes = juegos.count { it.estado == EstadoJuego.PENDIENTE }
    val completados = juegos.count { it.estado == EstadoJuego.COMPLETADO }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Jugando" to jugando, "Pendientes" to pendientes, "Completados" to completados).forEach { (etiqueta, total) ->
            Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(vertical = 14.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(total.toString(), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    Text(etiqueta, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
