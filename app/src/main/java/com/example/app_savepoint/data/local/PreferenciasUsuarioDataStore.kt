package com.example.app_savepoint.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app_savepoint.domain.model.Acento
import com.example.app_savepoint.domain.model.AjustesUsuario
import com.example.app_savepoint.domain.model.OrdenBiblioteca
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.preferenciasSavePoint by preferencesDataStore(name = "preferencias_savepoint")

class PreferenciasUsuarioDataStore(private val contexto: Context) {
    private object Claves {
        val acento = stringPreferencesKey("acento")
        val ordenBiblioteca = stringPreferencesKey("orden_biblioteca")
    }

    val ajustes: Flow<AjustesUsuario> = contexto.preferenciasSavePoint.data
        .catch { error ->
            if (error is IOException) emit(androidx.datastore.preferences.core.emptyPreferences())
            else throw error
        }
        .map { preferencias ->
            AjustesUsuario(
                acento = preferencias[Claves.acento]?.let { runCatching { Acento.valueOf(it) }.getOrNull() }
                    ?: Acento.MORADO,
                ordenBiblioteca = preferencias[Claves.ordenBiblioteca]
                    ?.let { runCatching { OrdenBiblioteca.valueOf(it) }.getOrNull() }
                    ?: OrdenBiblioteca.RECIENTES
            )
        }

    suspend fun guardarAcento(acento: Acento) {
        contexto.preferenciasSavePoint.edit { it[Claves.acento] = acento.name }
    }

    suspend fun guardarOrden(orden: OrdenBiblioteca) {
        contexto.preferenciasSavePoint.edit { it[Claves.ordenBiblioteca] = orden.name }
    }
}
