package com.example.app_savepoint

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_savepoint.data.local.SavePointDatabase
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import com.example.app_savepoint.data.local.PreferenciasUsuarioDataStore
import com.example.app_savepoint.data.remote.FreeToGameApi
import com.example.app_savepoint.ui.estado.LoadState
import com.example.app_savepoint.ui.navegacion.BarraNavegacion
import com.example.app_savepoint.ui.navegacion.Destino
import com.example.app_savepoint.ui.pantallas.PantallaAjustes
import com.example.app_savepoint.ui.pantallas.PantallaBiblioteca
import com.example.app_savepoint.ui.pantallas.PantallaDetalle
import com.example.app_savepoint.ui.pantallas.PantallaDiario
import com.example.app_savepoint.ui.pantallas.PantallaExplorar
import com.example.app_savepoint.ui.theme.SavePointTheme
import com.example.app_savepoint.ui.viewmodel.AjustesViewModel
import com.example.app_savepoint.ui.viewmodel.DiarioViewModel
import com.example.app_savepoint.ui.viewmodel.JuegoViewModel
import com.example.app_savepoint.ui.viewmodel.SavePointViewModelFactory

@Composable
fun SavePointApp(
    baseDatos: SavePointDatabase,
    preferencias: PreferenciasUsuarioDataStore,
    api: FreeToGameApi
) {
    val fabrica = remember(baseDatos, preferencias, api) { SavePointViewModelFactory(baseDatos, preferencias, api) }
    val juegoViewModel: JuegoViewModel = viewModel(factory = fabrica)
    val diarioViewModel: DiarioViewModel = viewModel(factory = fabrica)
    val ajustesViewModel: AjustesViewModel = viewModel(factory = fabrica)
    val biblioteca by juegoViewModel.biblioteca.collectAsStateWithLifecycle()
    val sesiones by diarioViewModel.sesiones.collectAsStateWithLifecycle()
    val ajustes by ajustesViewModel.ajustes.collectAsStateWithLifecycle()
    val catalogo by juegoViewModel.catalogo.collectAsStateWithLifecycle()
    val detalle by juegoViewModel.detalle.collectAsStateWithLifecycle()
    val bibliotecaOrdenada = remember(biblioteca, ajustes.ordenBiblioteca) {
        when (ajustes.ordenBiblioteca) {
            OrdenBiblioteca.RECIENTES -> biblioteca.sortedByDescending { it.fechaAgregado }
            OrdenBiblioteca.TITULO -> biblioteca.sortedBy { it.titulo.lowercase() }
            OrdenBiblioteca.PROGRESO -> biblioteca.sortedByDescending { it.progreso }
        }
    }

    SavePointTheme(acento = ajustes.acento) {
        val navController = rememberNavController()
        val entrada by navController.currentBackStackEntryAsState()
        val rutaActual = entrada?.destination?.route
        val mostrarBarra = rutaActual in Destino.principales.map { it.ruta }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (mostrarBarra) {
                    BarraNavegacion(rutaActual) { destino ->
                        navController.navigate(destino.ruta) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Destino.Explorar.ruta,
                modifier = Modifier.padding(padding)
            ) {
                composable(Destino.Explorar.ruta) {
                    PantallaExplorar(
                        estado = catalogo,
                        alReintentar = juegoViewModel::cargarCatalogo,
                        alAbrirDetalle = { navController.navigate(Destino.detalle(it)) }
                    )
                }
                composable(Destino.Biblioteca.ruta) {
                    PantallaBiblioteca(bibliotecaOrdenada, juegoViewModel::actualizarProgreso, juegoViewModel::eliminar)
                }
                composable(Destino.Diario.ruta) {
                    PantallaDiario(sesiones, biblioteca) { juego, duracion, progreso, nota ->
                        diarioViewModel.registrar(juego.juegoId, juego.titulo, duracion, progreso, nota)
                        juegoViewModel.actualizarProgreso(juego, progreso)
                    }
                }
                composable(Destino.Ajustes.ruta) {
                    PantallaAjustes(
                        acento = ajustes.acento,
                        orden = ajustes.ordenBiblioteca,
                        alSeleccionarAcento = ajustesViewModel::seleccionarAcento,
                        alSeleccionarOrden = ajustesViewModel::seleccionarOrden
                    )
                }
                composable(
                    route = Destino.DETALLE,
                    arguments = listOf(navArgument("juegoId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val juegoId = backStackEntry.arguments?.getInt("juegoId") ?: 0
                    LaunchedEffect(juegoId) { juegoViewModel.cargarDetalle(juegoId) }
                    DisposableEffect(juegoId) {
                        onDispose { juegoViewModel.limpiarDetalle() }
                    }
                    val objetivos by remember(juegoId) { juegoViewModel.observarObjetivos(juegoId) }
                        .collectAsStateWithLifecycle(initialValue = emptyList())
                    PantallaDetalle(
                        estado = detalle ?: LoadState.Loading,
                        guardado = biblioteca.any { it.juegoId == juegoId },
                        objetivos = objetivos,
                        alVolver = navController::navigateUp,
                        alReintentar = { juegoViewModel.cargarDetalle(juegoId) },
                        alGuardar = { juegoViewModel.guardar(it.juego) },
                        alAgregarObjetivo = { juegoViewModel.agregarObjetivo(juegoId, it) },
                        alAlternarObjetivo = juegoViewModel::alternarObjetivo
                    )
                }
            }
        }
    }
}
