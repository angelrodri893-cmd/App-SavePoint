package com.example.app_savepoint.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_savepoint.ui.componentes.CampoBusqueda
import com.example.app_savepoint.ui.componentes.EncabezadoSavePoint
import com.example.app_savepoint.ui.componentes.TarjetaJuego
import com.example.app_savepoint.ui.modelo.juegosDemostracion

@Composable
fun PantallaExplorar(alAbrirDetalle: (Int) -> Unit) {
    var consulta by remember { mutableStateOf("") }
    val juegos = remember(consulta) {
        juegosDemostracion.filter { it.titulo.contains(consulta, ignoreCase = true) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { EncabezadoSavePoint() }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("Explorar", style = androidx.compose.material3.MaterialTheme.typography.headlineLarge)
                CampoBusqueda(consulta, { consulta = it }, Modifier.padding(top = 18.dp))
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = { Text("Todos") },
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        items(juegos, key = { it.id }) { juego ->
            TarjetaJuego(
                juego = juego,
                alSeleccionar = { alAbrirDetalle(juego.id) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
