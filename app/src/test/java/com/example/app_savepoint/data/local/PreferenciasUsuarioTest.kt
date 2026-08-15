package com.example.app_savepoint.data.local

import com.example.app_savepoint.domain.model.Acento
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import org.junit.Assert.assertEquals
import org.junit.Test

class PreferenciasUsuarioTest {
    @Test
    fun `restaura acento y orden validos`() {
        val ajustes = decodificarAjustes("VERDE", "PROGRESO")

        assertEquals(Acento.VERDE, ajustes.acento)
        assertEquals(OrdenBiblioteca.PROGRESO, ajustes.ordenBiblioteca)
    }

    @Test
    fun `usa valores seguros si datastore contiene datos desconocidos`() {
        val ajustes = decodificarAjustes("AZUL", "ALEATORIO")

        assertEquals(Acento.MORADO, ajustes.acento)
        assertEquals(OrdenBiblioteca.RECIENTES, ajustes.ordenBiblioteca)
    }
}
