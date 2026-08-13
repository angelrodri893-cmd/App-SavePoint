package com.example.app_savepoint.domain.model

enum class Acento(val etiqueta: String) {
    MORADO("Morado"),
    VERDE("Verde"),
    ROJO("Rojo")
}

enum class OrdenBiblioteca(val etiqueta: String) {
    RECIENTES("Recientes"),
    TITULO("Título"),
    PROGRESO("Progreso")
}

data class AjustesUsuario(
    val acento: Acento = Acento.MORADO,
    val ordenBiblioteca: OrdenBiblioteca = OrdenBiblioteca.RECIENTES
)
