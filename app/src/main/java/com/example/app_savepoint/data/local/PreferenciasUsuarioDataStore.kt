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

interface PreferenciasFuenteLocal {
    val ajustes: Flow<AjustesUsuario>
    suspend fun guardarAcento(acento: Acento)
    suspend fun guardarOrden(orden: OrdenBiblioteca)
}

class PreferenciasUsuarioDataStore(private val contexto: Context) : PreferenciasFuenteLocal {
    private object Claves {
        val acento = stringPreferencesKey("acento")
        val ordenBiblioteca = stringPreferencesKey("orden_biblioteca")
    }

    override val ajustes: Flow<AjustesUsuario> = contexto.preferenciasSavePoint.data
        .catch { error ->
            if (error is IOException) emit(androidx.datastore.preferences.core.emptyPreferences())
            else throw error
        }
        .map { preferencias ->
            decodificarAjustes(preferencias[Claves.acento], preferencias[Claves.ordenBiblioteca])
        }

    override suspend fun guardarAcento(acento: Acento) {
        contexto.preferenciasSavePoint.edit { it[Claves.acento] = acento.name }
    }

    override suspend fun guardarOrden(orden: OrdenBiblioteca) {
        contexto.preferenciasSavePoint.edit { it[Claves.ordenBiblioteca] = orden.name }
    }
}

internal fun decodificarAjustes(acento: String?, orden: String?): AjustesUsuario = AjustesUsuario(
    acento = acento?.let { runCatching { Acento.valueOf(it) }.getOrNull() } ?: Acento.MORADO,
    ordenBiblioteca = orden?.let { runCatching { OrdenBiblioteca.valueOf(it) }.getOrNull() }
        ?: OrdenBiblioteca.RECIENTES
)
