package com.example.app_savepoint.data.repository

import com.example.app_savepoint.data.local.JuegoDao
import com.example.app_savepoint.data.local.JuegoGuardado
import com.example.app_savepoint.data.local.ObjetivoDao
import com.example.app_savepoint.data.local.ObjetivoJuego
import com.example.app_savepoint.data.local.PreferenciasFuenteLocal
import com.example.app_savepoint.data.local.SesionDao
import com.example.app_savepoint.data.local.SesionJuego
import com.example.app_savepoint.data.remote.FreeToGameApi
import com.example.app_savepoint.data.remote.JuegoDetalleDto
import com.example.app_savepoint.data.remote.JuegoRemotoDto
import com.example.app_savepoint.domain.model.Acento
import com.example.app_savepoint.domain.model.AjustesUsuario
import com.example.app_savepoint.domain.model.EstadoJuego
import com.example.app_savepoint.domain.model.Juego
import com.example.app_savepoint.domain.model.JuegoBiblioteca
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SavePointRepositoryImplTest {
    private val juegoDao = FakeJuegoDao()
    private val sesionDao = FakeSesionDao()
    private val objetivoDao = FakeObjetivoDao()
    private val api = FakeApi()
    private val repository = SavePointRepositoryImpl(juegoDao, sesionDao, objetivoDao, FakePreferencias(), api)

    @Test
    fun `combina catalogo remoto con modelo de dominio`() = runTest {
        api.catalogo = listOf(juegoDto(40, "Skyforge"))

        val juegos = repository.obtenerCatalogo()

        assertEquals(40, juegos.first().id)
        assertEquals("Skyforge", juegos.first().titulo)
    }

    @Test
    fun `guardar juego conserva snapshot para uso sin conexion`() = runTest {
        repository.guardarJuego(Juego(5, "Demo", "RPG", "PC", "https://img/demo.jpg"))

        assertEquals("Demo", juegoDao.guardado?.titulo)
        assertEquals("https://img/demo.jpg", juegoDao.guardado?.imagenUrl)
    }

    @Test
    fun `registrar sesion actualiza progreso local`() = runTest {
        val juego = JuegoBiblioteca(8, "Local", "", "Action", "PC", EstadoJuego.PENDIENTE, 0, 1)

        repository.registrarSesion(juego, 45, 30, "Primer jefe")

        assertEquals(45, sesionDao.insertada?.duracionMinutos)
        assertEquals(30, juegoDao.actualizado?.progreso)
        assertEquals(EstadoJuego.JUGANDO, juegoDao.actualizado?.estado)
    }
}

private class FakeJuegoDao : JuegoDao {
    private val datos = MutableStateFlow<List<JuegoGuardado>>(emptyList())
    var guardado: JuegoGuardado? = null
    var actualizado: JuegoGuardado? = null

    override fun observarBiblioteca(): Flow<List<JuegoGuardado>> = datos
    override fun observarJuego(juegoId: Int): Flow<JuegoGuardado?> = datos.map { lista -> lista.firstOrNull { it.juegoId == juegoId } }
    override suspend fun guardar(juego: JuegoGuardado) {
        guardado = juego
        datos.value = datos.value.filterNot { it.juegoId == juego.juegoId } + juego
    }
    override suspend fun actualizar(juego: JuegoGuardado) {
        actualizado = juego
        datos.value = datos.value.map { if (it.juegoId == juego.juegoId) juego else it }
    }
    override suspend fun eliminar(juego: JuegoGuardado) {
        datos.value = datos.value.filterNot { it.juegoId == juego.juegoId }
    }
}

private class FakeSesionDao : SesionDao {
    private val datos = MutableStateFlow<List<SesionJuego>>(emptyList())
    var insertada: SesionJuego? = null
    override fun observarSesiones(): Flow<List<SesionJuego>> = datos
    override fun observarSesionesDeJuego(juegoId: Int): Flow<List<SesionJuego>> = datos.map { lista -> lista.filter { it.juegoId == juegoId } }
    override suspend fun insertar(sesion: SesionJuego): Long {
        insertada = sesion.copy(sesionId = 1)
        datos.value = datos.value + requireNotNull(insertada)
        return 1
    }
    override suspend fun eliminar(sesion: SesionJuego) {
        datos.value = datos.value.filterNot { it.sesionId == sesion.sesionId }
    }
}

private class FakeObjetivoDao : ObjetivoDao {
    private val datos = MutableStateFlow<List<ObjetivoJuego>>(emptyList())
    override fun observarObjetivos(juegoId: Int): Flow<List<ObjetivoJuego>> = datos.map { lista -> lista.filter { it.juegoId == juegoId } }
    override suspend fun guardar(objetivo: ObjetivoJuego): Long {
        datos.value = datos.value + objetivo.copy(objetivoId = 1)
        return 1
    }
    override suspend fun actualizar(objetivo: ObjetivoJuego) {
        datos.value = datos.value.map { if (it.objetivoId == objetivo.objetivoId) objetivo else it }
    }
    override suspend fun eliminar(objetivo: ObjetivoJuego) {
        datos.value = datos.value.filterNot { it.objetivoId == objetivo.objetivoId }
    }
}

private class FakePreferencias : PreferenciasFuenteLocal {
    override val ajustes = MutableStateFlow(AjustesUsuario())
    override suspend fun guardarAcento(acento: Acento) { ajustes.value = ajustes.value.copy(acento = acento) }
    override suspend fun guardarOrden(orden: OrdenBiblioteca) { ajustes.value = ajustes.value.copy(ordenBiblioteca = orden) }
}

private class FakeApi : FreeToGameApi {
    var catalogo: List<JuegoRemotoDto> = emptyList()
    override suspend fun obtenerJuegos(): List<JuegoRemotoDto> = catalogo
    override suspend fun obtenerDetalle(juegoId: Int): JuegoDetalleDto = error("No requerido en esta prueba")
}

private fun juegoDto(id: Int, titulo: String) = JuegoRemotoDto(
    id = id,
    title = titulo,
    thumbnail = "https://img/$id.jpg",
    shortDescription = "Breve",
    gameUrl = "https://game/$id",
    genre = "Action",
    platform = "PC (Windows)",
    publisher = "Editorial",
    developer = "Estudio",
    releaseDate = "2026-01-01",
    profileUrl = "https://profile/$id"
)
