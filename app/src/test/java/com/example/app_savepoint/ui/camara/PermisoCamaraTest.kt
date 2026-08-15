package com.example.app_savepoint.ui.camara

import org.junit.Assert.assertEquals
import org.junit.Test

class PermisoCamaraTest {
    @Test
    fun `permiso concedido habilita camara`() {
        assertEquals(EstadoPermisoCamara.CONCEDIDO, resolverEstadoPermiso(true, false, true))
    }

    @Test
    fun `rechazo recuperable muestra justificacion`() {
        assertEquals(EstadoPermisoCamara.RECHAZADO, resolverEstadoPermiso(false, true, true))
    }

    @Test
    fun `rechazo sin justificacion despues de solicitar es permanente`() {
        assertEquals(EstadoPermisoCamara.RECHAZADO_PERMANENTE, resolverEstadoPermiso(false, false, true))
    }
}
