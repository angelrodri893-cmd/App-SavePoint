package com.example.app_savepoint.ui.modelo

import com.example.app_savepoint.data.remote.JuegoDetalleDto
import com.example.app_savepoint.data.remote.JuegoRemotoDto

data class JuegoVista(
    val id: Int,
    val titulo: String,
    val genero: String,
    val plataforma: String,
    val imagenUrl: String = "",
    val descripcionCorta: String = "",
    val desarrollador: String = "",
    val fechaLanzamiento: String = "",
    val progreso: Int = 0,
    val estado: String = "Pendiente"
)

data class JuegoDetalleVista(
    val juego: JuegoVista,
    val descripcion: String,
    val capturas: List<String>,
    val requisitos: List<Pair<String, String>>
)

fun JuegoRemotoDto.aVista() = JuegoVista(
    id = id,
    titulo = title,
    genero = genre,
    plataforma = platform,
    imagenUrl = thumbnail,
    descripcionCorta = shortDescription,
    desarrollador = developer,
    fechaLanzamiento = releaseDate
)

fun JuegoDetalleDto.aVista() = JuegoDetalleVista(
    juego = JuegoVista(
        id = id,
        titulo = title,
        genero = genre,
        plataforma = platform,
        imagenUrl = thumbnail,
        descripcionCorta = shortDescription,
        desarrollador = developer,
        fechaLanzamiento = releaseDate
    ),
    descripcion = description,
    capturas = screenshots.map { it.image },
    requisitos = listOfNotNull(
        requisitos?.os?.let { "Sistema" to it },
        requisitos?.processor?.let { "Procesador" to it },
        requisitos?.memory?.let { "Memoria" to it },
        requisitos?.graphics?.let { "Gráficos" to it },
        requisitos?.storage?.let { "Almacenamiento" to it }
    )
)

val juegosDemostracion = listOf(
    JuegoVista(1, "Hollow Knight", "Metroidvania - Acción", "PC"),
    JuegoVista(2, "Hades", "Roguelike - Acción", "PC"),
    JuegoVista(3, "Celeste", "Plataformas - Indie", "PC"),
    JuegoVista(4, "Stardew Valley", "Simulación - RPG", "PC")
)
