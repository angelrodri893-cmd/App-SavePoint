package com.example.app_savepoint.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [JuegoGuardado::class, SesionJuego::class, ObjetivoJuego::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Convertidores::class)
abstract class SavePointDatabase : RoomDatabase() {
    abstract fun juegoDao(): JuegoDao
    abstract fun sesionDao(): SesionDao
    abstract fun objetivoDao(): ObjetivoDao

    companion object {
        @Volatile
        private var instancia: SavePointDatabase? = null

        fun obtener(contexto: Context): SavePointDatabase = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                contexto.applicationContext,
                SavePointDatabase::class.java,
                "savepoint.db"
            ).build().also { instancia = it }
        }
    }
}
