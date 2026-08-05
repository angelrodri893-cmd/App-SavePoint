package com.example.app_savepoint.ui.viewmodel

import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.soporte.FakeSavePointRepository
import com.example.app_savepoint.soporte.MainDispatcherRule
import com.example.app_savepoint.ui.estado.LoadState
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class JuegoViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `carga catalogo desde el repository`() = runTest {
        val repository = FakeSavePointRepository().apply {
            catalogo = Result.success(listOf(Juego(1, "Demo", "RPG", "PC")))
        }

        val viewModel = JuegoViewModel(repository)

        val estado = viewModel.catalogo.value as LoadState.Content
        assertEquals("Demo", estado.data.first().titulo)
    }

    @Test
    fun `muestra error comprensible cuando no hay conexion`() = runTest {
        val repository = FakeSavePointRepository().apply {
            catalogo = Result.failure(IOException("sin red"))
        }

        val viewModel = JuegoViewModel(repository)

        val estado = viewModel.catalogo.value
        assertTrue(estado is LoadState.Error)
        assertTrue((estado as LoadState.Error).mensaje.contains("conexión"))
    }
}
