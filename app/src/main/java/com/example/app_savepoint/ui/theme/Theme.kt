package com.example.app_savepoint.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class Acento(val etiqueta: String, val color: Color, val contenedor: Color) {
    MORADO("Morado", MoradoAcento, MoradoContenedor),
    VERDE("Verde", VerdeAcento, VerdeContenedor),
    ROJO("Rojo", RojoAcento, RojoContenedor)
}

@Composable
fun SavePointTheme(
    acento: Acento = Acento.MORADO,
    content: @Composable () -> Unit
) {
    val colores = darkColorScheme(
        primary = acento.color,
        onPrimary = Color(0xFF24154B),
        primaryContainer = acento.contenedor,
        onPrimaryContainer = TextoPrincipal,
        secondary = VerdeAcento,
        tertiary = RojoAcento,
        background = FondoSavePoint,
        onBackground = TextoPrincipal,
        surface = SuperficieSavePoint,
        onSurface = TextoPrincipal,
        surfaceVariant = SuperficieElevada,
        onSurfaceVariant = TextoSecundario,
        outline = BordeSutil,
        error = RojoAcento
    )

    MaterialTheme(
        colorScheme = colores,
        typography = Typography,
        content = content
    )
}
