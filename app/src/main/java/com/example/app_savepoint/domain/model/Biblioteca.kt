package com.example.app_savepoint.domain.model

enum class EstadoJuego {
    PENDIENTE,
    JUGANDO,
    PAUSADO,
    COMPLETADO
}

data class JuegoBiblioteca(
    val juegoId: Int,
    val titulo: String,
    val imagenUrl: String,
    val genero: String,
    val plataforma: String,
    val estado: EstadoJuego,
    val progreso: Int,
    val fechaAgregado: Long
)

data class Sesion(
    val sesionId: Long,
    val juegoId: Int,
    val tituloJuego: String,
    val fecha: Long,
    val duracionMinutos: Int,
    val progreso: Int,
    val nota: String,
    val fotoUri: String?
)

data class Objetivo(
    val objetivoId: Long,
    val juegoId: Int,
    val descripcion: String,
    val completado: Boolean,
    val fechaCreacion: Long
)
