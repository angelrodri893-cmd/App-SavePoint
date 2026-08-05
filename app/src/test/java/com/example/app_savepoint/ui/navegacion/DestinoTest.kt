package com.example.app_savepoint.ui.navegacion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DestinoTest {
    @Test
    fun `destinos principales tienen rutas unicas`() {
        val rutas = Destino.principales.map { it.ruta }

        assertEquals(4, rutas.size)
        assertEquals(rutas.size, rutas.toSet().size)
    }

    @Test
    fun `detalle incluye identificador del juego`() {
        assertEquals("detalle/42", Destino.detalle(42))
        assertTrue(Destino.DETALLE.contains("{juegoId}"))
    }
}
