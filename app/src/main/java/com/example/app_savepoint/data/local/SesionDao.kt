package com.example.app_savepoint.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionDao {
    @Query("SELECT * FROM sesiones_juego ORDER BY fecha DESC")
    fun observarSesiones(): Flow<List<SesionJuego>>

    @Query("SELECT * FROM sesiones_juego WHERE juegoId = :juegoId ORDER BY fecha DESC")
    fun observarSesionesDeJuego(juegoId: Int): Flow<List<SesionJuego>>

    @Insert
    suspend fun insertar(sesion: SesionJuego): Long

    @Delete
    suspend fun eliminar(sesion: SesionJuego)
}
