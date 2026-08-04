package com.example.app_savepoint

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.app_savepoint.ui.navegacion.BarraNavegacion
import com.example.app_savepoint.ui.navegacion.Destino
import com.example.app_savepoint.ui.pantallas.PantallaAjustes
import com.example.app_savepoint.ui.pantallas.PantallaBiblioteca
import com.example.app_savepoint.ui.pantallas.PantallaDetalle
import com.example.app_savepoint.ui.pantallas.PantallaDiario
import com.example.app_savepoint.ui.pantallas.PantallaExplorar
import com.example.app_savepoint.ui.theme.Acento
import com.example.app_savepoint.ui.theme.SavePointTheme

@Composable
fun SavePointApp() {
    var acento by remember { mutableStateOf(Acento.MORADO) }
    SavePointTheme(acento = acento) {
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
                    PantallaExplorar { navController.navigate(Destino.detalle(it)) }
                }
                composable(Destino.Biblioteca.ruta) { PantallaBiblioteca() }
                composable(Destino.Diario.ruta) { PantallaDiario() }
                composable(Destino.Ajustes.ruta) { PantallaAjustes(acento) { acento = it } }
                composable(
                    route = Destino.DETALLE,
                    arguments = listOf(navArgument("juegoId") { type = NavType.IntType })
                ) { backStackEntry ->
                    PantallaDetalle(
                        juegoId = backStackEntry.arguments?.getInt("juegoId") ?: 0,
                        alVolver = navController::navigateUp
                    )
                }
            }
        }
    }
}
