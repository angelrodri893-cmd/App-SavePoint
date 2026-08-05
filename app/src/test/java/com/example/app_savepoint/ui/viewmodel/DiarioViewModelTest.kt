package com.example.app_savepoint.ui.viewmodel

import com.example.app_savepoint.domain.model.EstadoJuego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.soporte.FakeSavePointRepository
import com.example.app_savepoint.soporte.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class DiarioViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val juego = JuegoBiblioteca(1, "Demo", "", "RPG", "PC", EstadoJuego.JUGANDO, 10, 1)

    @Test
    fun `permite registrar sesion sin fotografia`() = runTest {
        val repository = FakeSavePointRepository()
        val viewModel = DiarioViewModel(repository)

        viewModel.registrar(juego, 60, 25, "Sesión normal")

        assertNull(repository.sesionRegistrada?.fotoUri)
        assertEquals(60, repository.sesionRegistrada?.duracionMinutos)
    }

    @Test
    fun `envia la referencia cuando hay fotografia`() = runTest {
        val repository = FakeSavePointRepository()
        val viewModel = DiarioViewModel(repository)

        viewModel.registrar(juego, 45, 30, "Con logro", "file:///foto.jpg")

        assertEquals("file:///foto.jpg", repository.sesionRegistrada?.fotoUri)
    }
}
