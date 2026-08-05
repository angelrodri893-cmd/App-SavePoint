package com.example.app_savepoint.domain.model

data class Juego(
    val id: Int,
    val titulo: String,
    val genero: String,
    val plataforma: String,
    val imagenUrl: String = "",
    val descripcionCorta: String = "",
    val desarrollador: String = "",
    val fechaLanzamiento: String = ""
)

data class DetalleJuego(
    val juego: Juego,
    val descripcion: String,
    val capturas: List<String>,
    val requisitos: List<Pair<String, String>>
)
