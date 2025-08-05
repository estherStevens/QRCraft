package com.stevens.software.qrcraft.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    surface = Surface,
    onSurface = OnSurface,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    surface = Surface,
    onSurface = OnSurface,
    error = Error
)

val localExtendedColors = compositionLocalOf { extendedColors }

@Composable
fun QRCraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(localExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val MaterialTheme.extendedColours: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = localExtendedColors.current