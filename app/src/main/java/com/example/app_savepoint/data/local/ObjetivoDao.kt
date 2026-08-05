package com.example.app_savepoint.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjetivoDao {
    @Query("SELECT * FROM objetivos_juego WHERE juegoId = :juegoId ORDER BY fechaCreacion ASC")
    fun observarObjetivos(juegoId: Int): Flow<List<ObjetivoJuego>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(objetivo: ObjetivoJuego): Long

    @Update
    suspend fun actualizar(objetivo: ObjetivoJuego)

    @Delete
    suspend fun eliminar(objetivo: ObjetivoJuego)
}
