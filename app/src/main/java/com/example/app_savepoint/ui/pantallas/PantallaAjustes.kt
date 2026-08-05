package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.app_savepoint.ui.componentes.EncabezadoSavePoint
import com.example.app_savepoint.data.local.OrdenBiblioteca
import com.example.app_savepoint.ui.theme.Acento

@Composable
fun PantallaAjustes(
    acento: Acento,
    orden: OrdenBiblioteca,
    alSeleccionarAcento: (Acento) -> Unit,
    alSeleccionarOrden: (OrdenBiblioteca) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        EncabezadoSavePoint()
        Text("Ajustes", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(16.dp))
        Card(modifier = Modifier.padding(16.dp).fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Color de acento", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Acento.entries.forEach { opcion ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier.size(54.dp).clip(CircleShape)
                                    .background(opcion.color).clickable { alSeleccionarAcento(opcion) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (opcion == acento) Icon(Icons.Default.Check, contentDescription = "Seleccionado")
                            }
                            Text(opcion.etiqueta, modifier = Modifier.padding(top = 6.dp))
                        }
                    }
                }
            }
        }
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Orden de biblioteca", style = MaterialTheme.typography.titleLarge)
                OrdenBiblioteca.entries.forEach { opcion ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { alSeleccionarOrden(opcion) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = opcion == orden, onClick = { alSeleccionarOrden(opcion) })
                        Text(opcion.etiqueta)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.SportsEsports, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Acerca de SavePoint", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 12.dp))
                Text("Versión 1.0", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Datos de juegos proporcionados por FreeToGame", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}
