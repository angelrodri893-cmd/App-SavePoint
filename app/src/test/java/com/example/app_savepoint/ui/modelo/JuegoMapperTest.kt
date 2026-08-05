package com.example.app_savepoint.ui.modelo

import com.example.app_savepoint.data.remote.JuegoRemotoDto
import org.junit.Assert.assertEquals
import org.junit.Test

class JuegoMapperTest {
    @Test
    fun `dto se convierte al modelo visible`() {
        val dto = JuegoRemotoDto(
            id = 12,
            title = "Juego de prueba",
            thumbnail = "https://img/juego.jpg",
            shortDescription = "Descripción",
            gameUrl = "https://game",
            genre = "RPG",
            platform = "Web Browser",
            publisher = "Editorial",
            developer = "Estudio",
            releaseDate = "2026-03-01",
            profileUrl = "https://profile"
        )

        val vista = dto.aVista()

        assertEquals(12, vista.id)
        assertEquals("Juego de prueba", vista.titulo)
        assertEquals("Web Browser", vista.plataforma)
        assertEquals("https://img/juego.jpg", vista.imagenUrl)
    }
}
