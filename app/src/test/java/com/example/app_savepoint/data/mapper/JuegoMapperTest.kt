package com.example.app_savepoint.data.mapper

import com.example.app_savepoint.data.remote.JuegoRemotoDto
import org.junit.Assert.assertEquals
import org.junit.Test

class JuegoMapperTest {
    @Test
    fun `dto se convierte al modelo de dominio`() {
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

        val juego = dto.aDominio()

        assertEquals(12, juego.id)
        assertEquals("Juego de prueba", juego.titulo)
        assertEquals("Web Browser", juego.plataforma)
        assertEquals("https://img/juego.jpg", juego.imagenUrl)
    }
}
