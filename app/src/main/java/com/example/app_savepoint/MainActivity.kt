package com.example.app_savepoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.app_savepoint.di.AppContainer

class MainActivity : ComponentActivity() {
    private val contenedor by lazy { AppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SavePointApp(contenedor.repository) }
    }
}
