// ruta: app/src/main/java/com/crossfit/timer/ui/theme/Theme.kt

package com.crossfit.timer.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- Esquema de colores para el Tema Oscuro ---
private val AppDarkColorScheme = darkColorScheme(
    primary = CrossFitRed,                  // Color para botones principales, iconos activos
    onPrimary = OffWhite,                   // Color del texto/iconos sobre el color primario
    secondary = GreyAccent,                 // Color para acentos secundarios
    onSecondary = DarkBackground,           // Texto sobre el color secundario
    background = DarkBackground,            // << El color de fondo de toda la app
    onBackground = OffWhite,                // << El color del texto por defecto
    surface = DarkSurface,                  // Color para Cards, Dialogs, Bottom Sheets
    onSurface = OffWhite,                   // Texto sobre esas superficies
    error = PausedYellow,                   // Usaremos el amarillo de pausa como color de error/aviso
    onError = DarkBackground
)

// --- Esquema de colores para un Tema Claro (opcional, pero buena práctica) ---
// Por ahora, lo haremos similar al oscuro para mantener consistencia.
private val AppLightColorScheme = lightColorScheme(
    primary = CrossFitRed,
    onPrimary = OffWhite,
    secondary = GreyAccent,
    onSecondary = DarkBackground,
    background = Color(0xFFFFFFFF), // Fondo blanco para el modo claro
    onBackground = DarkBackground,  // Texto oscuro sobre fondo blanco
    surface = Color(0xFFF8F8F8),
    onSurface = DarkBackground,
    error = PausedYellow,
    onError = DarkBackground
)

@Composable
fun CrossFitTimerTheme(
    // Forzamos el tema oscuro por defecto, ya que es nuestro diseño principal
    darkTheme: Boolean = true, // isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AppDarkColorScheme else AppLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Hacemos que la barra de estado superior sea del mismo color que el fondo
            window.statusBarColor = colorScheme.background.toArgb()
            // Le decimos al sistema que los iconos de la barra de estado (hora, batería) deben ser claros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // La tipografía que ya tenías definida
        content = content
    )
}
