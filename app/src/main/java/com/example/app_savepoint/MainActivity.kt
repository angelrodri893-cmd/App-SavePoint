package com.example.app_savepoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.app_savepoint.data.local.SavePointDatabase
import com.example.app_savepoint.data.local.PreferenciasUsuarioDataStore

class MainActivity : ComponentActivity() {
    private val baseDatos by lazy { SavePointDatabase.obtener(applicationContext) }
    private val preferencias by lazy { PreferenciasUsuarioDataStore(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SavePointApp(baseDatos, preferencias) }
    }
}
