package com.example.app_savepoint.ui.camara

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File

@Composable
fun CapturaCamara(
    alCapturar: (String) -> Unit,
    alCancelar: () -> Unit,
    alError: (String) -> Unit
) {
    val contexto = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }
    val proveedorFuture = remember { ProcessCameraProvider.getInstance(contexto) }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            if (proveedorFuture.isDone) proveedorFuture.get().unbindAll()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    proveedorFuture.addListener({
                        runCatching {
                            val proveedor = proveedorFuture.get()
                            val preview = Preview.Builder().build().also { it.surfaceProvider = surfaceProvider }
                            proveedor.unbindAll()
                            proveedor.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageCapture
                            )
                        }.onFailure { alError("No se pudo iniciar la cámara.") }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            },
            modifier = Modifier.fillMaxWidth().height(360.dp)
        )
        Button(
            onClick = { tomarFoto(contexto, imageCapture, alCapturar, alError) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null)
            Text("  Tomar foto")
        }
        TextButton(onClick = alCancelar, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
    }
}

private fun tomarFoto(
    contexto: Context,
    imageCapture: ImageCapture,
    alCapturar: (String) -> Unit,
    alError: (String) -> Unit
) {
    val carpeta = File(contexto.filesDir, "fotos_sesiones").apply { mkdirs() }
    val archivo = File(carpeta, "sesion_${System.currentTimeMillis()}.jpg")
    val opciones = ImageCapture.OutputFileOptions.Builder(archivo).build()
    imageCapture.takePicture(
        opciones,
        ContextCompat.getMainExecutor(contexto),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(resultado: ImageCapture.OutputFileResults) {
                alCapturar(Uri.fromFile(archivo).toString())
            }

            override fun onError(error: ImageCaptureException) {
                archivo.delete()
                alError("No se pudo guardar la fotografía.")
            }
        }
    )
}
