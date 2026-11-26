package com.example.parcial.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Azul40,
    onPrimary = Blanco,
    primaryContainer = Azul95,
    onPrimaryContainer = Azul30,
    secondary = Azul30,
    onSecondary = Blanco,
    secondaryContainer = Azul90,
    onSecondaryContainer = Azul10,
    background = Gris96,
    onBackground = Gris10,
    surface = Blanco,
    onSurface = Gris10,
    surfaceVariant = Gris90,
    onSurfaceVariant = Gris50,
    outline = Gris85,
    outlineVariant = Gris90,
    error = Rojo40,
    onError = Blanco,
    errorContainer = Rojo90,
    onErrorContainer = Rojo10
)

private val DarkColorScheme = darkColorScheme(
    primary = Azul80,
    onPrimary = Azul10,
    primaryContainer = Azul30,
    onPrimaryContainer = Azul90,
    secondary = Azul90,
    onSecondary = Azul10,
    secondaryContainer = Azul30,
    onSecondaryContainer = Azul90,
    background = Gris12,
    onBackground = Gris90,
    surface = Gris17,
    onSurface = Gris90,
    surfaceVariant = Gris30,
    onSurfaceVariant = Gris80,
    outline = Gris50,
    outlineVariant = Gris20,
    error = Rojo80,
    onError = Rojo20,
    errorContainer = Rojo30,
    onErrorContainer = Rojo90
)

@Composable
fun ParcialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
