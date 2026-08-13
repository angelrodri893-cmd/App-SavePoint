package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_savepoint.ui.componentes.CampoBusqueda
import com.example.app_savepoint.ui.componentes.EncabezadoSavePoint
import com.example.app_savepoint.ui.componentes.EstadoVacio
import com.example.app_savepoint.ui.componentes.TarjetaJuego
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.ui.estado.LoadState

@Composable
fun PantallaExplorar(
    estado: LoadState<List<Juego>>,
    alReintentar: () -> Unit,
    alAbrirDetalle: (Int) -> Unit
) {
    var consulta by remember { mutableStateOf("") }
    var plataforma by remember { mutableStateOf("Todos") }
    Column(Modifier.fillMaxSize()) {
        EncabezadoSavePoint()
        Text("Explorar", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(horizontal = 16.dp))
        CampoBusqueda(consulta, { consulta = it }, Modifier.padding(horizontal = 16.dp, vertical = 14.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("Todos", "PC", "Navegador")) { opcion ->
                FilterChip(
                    selected = plataforma == opcion,
                    onClick = { plataforma = opcion },
                    label = { Text(opcion) }
                )
            }
        }
        when (estado) {
            LoadState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is LoadState.Error -> ErrorCatalogo(estado.mensaje, alReintentar)
            is LoadState.Content -> {
                val juegosFiltrados = remember(estado.data, consulta, plataforma) {
                    estado.data.filter { juego ->
                        juego.titulo.contains(consulta.trim(), ignoreCase = true) && when (plataforma) {
                            "PC" -> juego.plataforma.contains("PC", ignoreCase = true)
                            "Navegador" -> juego.plataforma.contains("Web", ignoreCase = true)
                            else -> true
                        }
                    }
                }
                if (juegosFiltrados.isEmpty()) {
                    EstadoVacio("Sin resultados", "Prueba con otro nombre o cambia el filtro de plataforma.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(juegosFiltrados, key = { it.id }) { juego ->
                            TarjetaJuego(
                                juego = juego,
                                alSeleccionar = { alAbrirDetalle(juego.id) },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        item {
                            Text(
                                "Datos proporcionados por FreeToGame",
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorCatalogo(mensaje: String, alReintentar: () -> Unit) {
    Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp), //esta seccion sirve para que el texto no se corte
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Text("Conexión perdida", style = MaterialTheme.typography.titleLarge)
            Text(mensaje, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = alReintentar) { Text("Reintentar") }
        }
    }
}
