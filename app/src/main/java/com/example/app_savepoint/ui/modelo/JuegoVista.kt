package com.example.app_savepoint.ui.modelo

data class JuegoVista(
    val id: Int,
    val titulo: String,
    val genero: String,
    val plataforma: String,
    val progreso: Int = 0,
    val estado: String = "Pendiente"
)

val juegosDemostracion = listOf(
    JuegoVista(1, "Hollow Knight", "Metroidvania - Acción", "PC"),
    JuegoVista(2, "Hades", "Roguelike - Acción", "PC"),
    JuegoVista(3, "Celeste", "Plataformas - Indie", "PC"),
    JuegoVista(4, "Stardew Valley", "Simulación - RPG", "PC")
)
