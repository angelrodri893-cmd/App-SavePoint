package com.example.app_savepoint.data.mapper

import com.example.app_savepoint.data.remote.JuegoDetalleDto
import com.example.app_savepoint.data.remote.JuegoRemotoDto
import com.example.app_savepoint.domain.model.DetalleJuego
import com.example.app_savepoint.domain.model.Juego

fun JuegoRemotoDto.aDominio() = Juego(
    id = id,
    titulo = title,
    genero = genre,
    plataforma = platform,
    imagenUrl = thumbnail,
    descripcionCorta = shortDescription,
    desarrollador = developer,
    fechaLanzamiento = releaseDate
)

fun JuegoDetalleDto.aDominio() = DetalleJuego(
    juego = Juego(
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
