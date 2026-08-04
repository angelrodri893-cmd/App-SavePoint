package com.example.app_savepoint.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JuegoDao {
    @Query("SELECT * FROM juegos_guardados ORDER BY fechaAgregado DESC")
    fun observarBiblioteca(): Flow<List<JuegoGuardado>>

    @Query("SELECT * FROM juegos_guardados WHERE juegoId = :juegoId LIMIT 1")
    fun observarJuego(juegoId: Int): Flow<JuegoGuardado?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(juego: JuegoGuardado)

    @Update
    suspend fun actualizar(juego: JuegoGuardado)

    @Delete
    suspend fun eliminar(juego: JuegoGuardado)
}
