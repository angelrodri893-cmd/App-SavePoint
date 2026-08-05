package com.example.app_savepoint.ui.camara

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

enum class EstadoPermisoCamara {
    CONCEDIDO,
    SIN_SOLICITAR,
    RECHAZADO,
    RECHAZADO_PERMANENTE
}

data class ControlPermisoCamara(
    val estado: EstadoPermisoCamara,
    val solicitar: () -> Unit
)

@Composable
fun recordarControlPermisoCamara(): ControlPermisoCamara {
    val contexto = LocalContext.current
    val actividad = contexto.encontrarActividad()
    var solicitado by remember { mutableStateOf(false) }
    var estado by remember {
        mutableStateOf(
            if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                EstadoPermisoCamara.CONCEDIDO
            } else {
                EstadoPermisoCamara.SIN_SOLICITAR
            }
        )
    }
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        estado = when {
            concedido -> EstadoPermisoCamara.CONCEDIDO
            actividad?.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) == true ->
                EstadoPermisoCamara.RECHAZADO
            solicitado -> EstadoPermisoCamara.RECHAZADO_PERMANENTE
            else -> EstadoPermisoCamara.RECHAZADO
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            estado = EstadoPermisoCamara.CONCEDIDO
        }
    }

    return ControlPermisoCamara(estado = estado) {
        solicitado = true
        lanzador.launch(Manifest.permission.CAMERA)
    }
}

private tailrec fun Context.encontrarActividad(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.encontrarActividad()
    else -> null
}
