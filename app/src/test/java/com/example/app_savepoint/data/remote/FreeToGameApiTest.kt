package com.example.app_savepoint.data.remote

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FreeToGameApiTest {
    private lateinit var servidor: MockWebServer
    private lateinit var api: FreeToGameApi

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        api = Retrofit.Builder()
            .baseUrl(servidor.url("/api/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FreeToGameApi::class.java)
    }

    @After
    fun cerrar() {
        servidor.shutdown()
    }

    @Test
    fun `catalogo convierte respuesta exitosa`() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """[{"id":7,"title":"Demo","thumbnail":"https://img/demo.jpg","short_description":"Breve","game_url":"https://game","genre":"Action","platform":"PC (Windows)","publisher":"Pub","developer":"Dev","release_date":"2026-01-01","freetogame_profile_url":"https://profile"}]"""
            )
        )

        val juegos = api.obtenerJuegos()

        assertEquals(1, juegos.size)
        assertEquals("Demo", juegos.first().title)
        assertEquals("Breve", juegos.first().shortDescription)
        assertEquals("/api/games", servidor.takeRequest().path)
    }

    @Test
    fun `catalogo propaga error http`() {
        servidor.enqueue(MockResponse().setResponseCode(503))

        assertThrows(HttpException::class.java) {
            runBlocking { api.obtenerJuegos() }
        }
    }
}
