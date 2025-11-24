package com.crossfit.timer.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Paleta de colores para el tema oscuro con la nueva identidad visual
private val DarkColorScheme = darkColorScheme(
    primary = VibrantTurquoise,
    secondary = EnergeticMagenta,
    tertiary = HighlightYellow,
    background = DarkBackground,
    surface = DarkBackground.copy(alpha = 0.9f),
    onPrimary = Black,
    onSecondary = White,
    onTertiary = Black,
    onBackground = White,
    onSurface = White
)

// Paleta de colores para el tema claro con la nueva identidad visual
private val LightColorScheme = lightColorScheme(
    primary = EnergeticMagenta,
    secondary = VibrantTurquoise,
    tertiary = HighlightYellow,
    background = LightBackground,
    surface = White,
    onPrimary = White,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun CrossFitTimerTheme(
    darkTheme: Boolean = true, // << FORZAMOS EL TEMA OSCURO POR DEFECTO
    // Forzamos nuestro tema personalizado desactivando los colores dinámicos
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Esta sección ahora no se ejecutará, pero la dejamos por si se reactiva en el futuro
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
