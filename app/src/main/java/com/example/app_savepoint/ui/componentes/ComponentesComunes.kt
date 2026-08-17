package com.example.app_savepoint.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.ui.theme.TarjetaOscura
import coil.compose.SubcomposeAsyncImage

@Composable
fun EncabezadoSavePoint(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SavePoint",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EstadoVacio(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(64.dp).clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SportsEsports, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(16.dp))
        Text(titulo, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun TarjetaJuego(
    juego: Juego,
    alSeleccionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = alSeleccionar),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = TarjetaOscura)
    ) {
        Row(modifier = Modifier.height(144.dp)) {
            Box(
                modifier = Modifier.size(width = 144.dp, height = 144.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (juego.imagenUrl.isBlank()) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = null,
                        modifier = Modifier.size(42.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = juego.imagenUrl,
                        contentDescription = "Portada de ${juego.titulo}",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop,
                        error = {
                            Box(Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = null,
                                    modifier = Modifier.size(42.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = juego.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = juego.genero,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (juego.plataforma.contains("Web", ignoreCase = true)) {
                            Icons.Default.Language
                        } else {
                            Icons.Default.DesktopWindows
                        },
                        contentDescription = "Disponible en ${juego.plataforma}",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text("  ${juego.plataforma}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun CampoBusqueda(
    valor: String,
    alCambiar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        placeholder = { Text("Buscar videojuegos") }
    )
}
