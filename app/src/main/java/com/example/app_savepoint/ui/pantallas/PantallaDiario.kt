package com.example.app_savepoint.ui.pantallas

import android.content.Intent
import android.net.Uri
import android.provider.Settings

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.Sesion
import com.example.app_savepoint.ui.componentes.EncabezadoSavePoint
import com.example.app_savepoint.ui.componentes.EstadoVacio
import com.example.app_savepoint.ui.camara.CapturaCamara
import com.example.app_savepoint.ui.camara.EstadoPermisoCamara
import com.example.app_savepoint.ui.camara.recordarControlPermisoCamara
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun PantallaDiario(
    sesiones: List<Sesion>,
    biblioteca: List<JuegoBiblioteca>,
    alRegistrar: (JuegoBiblioteca, Int, Int, String, String?) -> Unit
) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    val permisoCamara = recordarControlPermisoCamara()
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
            EstadoVacio(
                "Aún no hay sesiones",
                if (biblioteca.isEmpty()) "Agrega primero un juego a tu biblioteca."
                else "Registra una sesión para recordar qué jugaste y cuánto avanzaste."
            )
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
            biblioteca = biblioteca,
            estadoPermiso = permisoCamara.estado,
            alSolicitarPermiso = permisoCamara.solicitar,
            alCerrar = { mostrarFormulario = false },
            alGuardar = { juego, duracion, progreso, nota, fotoUri ->
                alRegistrar(juego, duracion, progreso, nota, fotoUri)
                mostrarFormulario = false
            }
        )
    }
}

@Composable
private fun TarjetaSesion(sesion: Sesion) {
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
            sesion.fotoUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Fotografía de la sesión de ${sesion.tituloJuego}",
                    modifier = Modifier.fillMaxWidth().height(180.dp).padding(top = 12.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FormularioSesion(
    biblioteca: List<JuegoBiblioteca>,
    estadoPermiso: EstadoPermisoCamara,
    alSolicitarPermiso: () -> Unit,
    alCerrar: () -> Unit,
    alGuardar: (JuegoBiblioteca, Int, Int, String, String?) -> Unit
) {
    val contexto = LocalContext.current
    var juegoSeleccionado by remember(biblioteca) { mutableStateOf(biblioteca.first()) }
    var selectorExpandido by remember { mutableStateOf(false) }
    var duracion by remember { mutableStateOf("60") }
    var progreso by remember { mutableStateOf(juegoSeleccionado.progreso.toString()) }
    var nota by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<String?>(null) }
    var mostrarCamara by remember { mutableStateOf(false) }
    var mensajeCamara by remember { mutableStateOf<String?>(null) }
    var abrirTrasPermiso by remember { mutableStateOf(false) }
    val cancelarFormulario = {
        eliminarFotoTemporal(fotoUri)
        alCerrar()
    }

    LaunchedEffect(estadoPermiso, abrirTrasPermiso) {
        if (abrirTrasPermiso && estadoPermiso == EstadoPermisoCamara.CONCEDIDO) {
            abrirTrasPermiso = false
            mostrarCamara = true
        }
    }

    if (mostrarCamara) {
        Dialog(onDismissRequest = { mostrarCamara = false }) {
            androidx.compose.material3.Surface(shape = RoundedCornerShape(18.dp)) {
                CapturaCamara(
                    alCapturar = {
                        if (fotoUri != it) eliminarFotoTemporal(fotoUri)
                        fotoUri = it
                        mensajeCamara = "Fotografía lista para guardar"
                        mostrarCamara = false
                    },
                    alCancelar = { mostrarCamara = false },
                    alError = {
                        mensajeCamara = it
                        mostrarCamara = false
                    }
                )
            }
        }
        return
    }
    AlertDialog(
        onDismissRequest = cancelarFormulario,
        title = { Text("Nueva sesión") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ExposedDropdownMenuBox(
                    expanded = selectorExpandido,
                    onExpandedChange = { selectorExpandido = !selectorExpandido }
                ) {
                    OutlinedTextField(
                        value = juegoSeleccionado.titulo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Juego") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = selectorExpandido)
                        },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = selectorExpandido,
                        onDismissRequest = { selectorExpandido = false }
                    ) {
                        biblioteca.forEach { juego ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(juego.titulo)
                                        Text(
                                            text = "Progreso guardado: ${juego.progreso}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    juegoSeleccionado = juego
                                    progreso = juego.progreso.toString()
                                    selectorExpandido = false
                                }
                            )
                        }
                    }
                }
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
                Button(onClick = {
                    when (estadoPermiso) {
                        EstadoPermisoCamara.CONCEDIDO -> mostrarCamara = true
                        EstadoPermisoCamara.RECHAZADO_PERMANENTE -> {
                            mensajeCamara = "El permiso está bloqueado. Puedes habilitarlo en Ajustes o guardar sin foto."
                        }
                        EstadoPermisoCamara.SIN_SOLICITAR,
                        EstadoPermisoCamara.RECHAZADO -> {
                            abrirTrasPermiso = true
                            alSolicitarPermiso()
                        }
                    }
                }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Text(if (fotoUri == null) "  Agregar foto" else "  Repetir foto")
                }
                mensajeCamara?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                if (estadoPermiso == EstadoPermisoCamara.RECHAZADO) {
                    Text("La foto es opcional. Puedes volver a solicitar el permiso o guardar la sesión sin ella.")
                }
                if (estadoPermiso == EstadoPermisoCamara.RECHAZADO_PERMANENTE) {
                    TextButton(onClick = {
                        contexto.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", contexto.packageName, null)
                            }
                        )
                    }) { Text("Abrir ajustes de la app") }
                }
                fotoUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Vista previa de la fotografía",
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    alGuardar(
                        juegoSeleccionado,
                        duracion.toIntOrNull() ?: 1,
                        progreso.toIntOrNull() ?: 0,
                        nota,
                        fotoUri
                    )
                }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = cancelarFormulario) { Text("Cancelar") } }
    )
}

private fun eliminarFotoTemporal(fotoUri: String?) {
    fotoUri?.let { uri ->
        Uri.parse(uri).path?.let { ruta -> runCatching { File(ruta).delete() } }
    }
}
