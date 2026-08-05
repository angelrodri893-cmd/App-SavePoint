package com.example.app_savepoint.data.local

import androidx.room.TypeConverter
import com.example.app_savepoint.domain.model.EstadoJuego

class Convertidores {
    @TypeConverter
    fun estadoAString(estado: EstadoJuego): String = estado.name

    @TypeConverter
    fun stringAEstado(valor: String): EstadoJuego = EstadoJuego.valueOf(valor)
}
