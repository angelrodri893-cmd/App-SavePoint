package com.example.app_savepoint.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class EstadoJuego {
    PENDIENTE,
    JUGANDO,
    PAUSADO,
    COMPLETADO
}

@Entity(tableName = "juegos_guardados")
data class JuegoGuardado(
    @PrimaryKey val juegoId: Int,
    val titulo: String,
    val imagenUrl: String,
    val genero: String,
    val plataforma: String,
    val estado: EstadoJuego = EstadoJuego.PENDIENTE,
    val progreso: Int = 0,
    val fechaAgregado: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "sesiones_juego",
    foreignKeys = [
        ForeignKey(
            entity = JuegoGuardado::class,
            parentColumns = ["juegoId"],
            childColumns = ["juegoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("juegoId")]
)
data class SesionJuego(
    @PrimaryKey(autoGenerate = true) val sesionId: Long = 0,
    val juegoId: Int,
    val tituloJuego: String,
    val fecha: Long = System.currentTimeMillis(),
    val duracionMinutos: Int,
    val progreso: Int,
    val nota: String,
    val fotoUri: String? = null
)

@Entity(
    tableName = "objetivos_juego",
    foreignKeys = [
        ForeignKey(
            entity = JuegoGuardado::class,
            parentColumns = ["juegoId"],
            childColumns = ["juegoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("juegoId")]
)
data class ObjetivoJuego(
    @PrimaryKey(autoGenerate = true) val objetivoId: Long = 0,
    val juegoId: Int,
    val descripcion: String,
    val completado: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)
