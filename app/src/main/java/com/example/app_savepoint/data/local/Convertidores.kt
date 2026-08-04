package com.example.app_savepoint.data.local

import androidx.room.TypeConverter

class Convertidores {
    @TypeConverter
    fun estadoAString(estado: EstadoJuego): String = estado.name

    @TypeConverter
    fun stringAEstado(valor: String): EstadoJuego = EstadoJuego.valueOf(valor)
}
